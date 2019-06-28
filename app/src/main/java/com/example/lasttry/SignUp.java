package com.example.lasttry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;



public class SignUp extends AppCompatActivity {
    Button btnGenerateOTP, btnSignIn;
    EditText etPhoneNumber, etOTP;
    String phoneNumber, otp;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    CountryCodePicker cpp;
    String CountryCode;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    EditText Uname;
    private FirebaseFunctions mFunctions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("UserInfo");

        cpp = findViewById(R.id.ccp);
        mFunctions=FirebaseFunctions.getInstance();
        findViews();
        StartFirebaseLogin();



        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = CountryCode;
                phoneNumber = temp.concat(etPhoneNumber.getText().toString());
                String refreshedToken= FirebaseInstanceId.getInstance().getToken();

             try {


                 checkToken(refreshedToken);
             }
             catch (Exception e)
                {
                    Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }



            }
        });


        onCountryPickerClick();

    }

    private void findViews() {
        btnGenerateOTP=findViewById(R.id.b1);
        btnSignIn=findViewById(R.id.b2);
        etPhoneNumber=findViewById(R.id.e2);
        etOTP=findViewById(R.id.e1);
        Uname=findViewById(R.id.Uname);

    }
    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(SignUp.this,"verification completed",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(SignUp.this,"not able to verify",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Intent i=new Intent(SignUp.this,Otp.class);
                i.putExtra("from","new");
                i.putExtra("Uname",Uname.getText().toString());
                i.putExtra("otp",verificationCode);
                i.putExtra("num",phoneNumber);
                Toast.makeText(SignUp.this,"Code sent",Toast.LENGTH_SHORT).show();

                startActivity(i);

                }
        };
    }



    //check  the token
    private Task<ArrayList<String>> checkToken(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("list", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("checkToken")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down
                        Toast.makeText(SignUp.this,task.getResult().getData().toString(),Toast.LENGTH_LONG).show();
                        if(task.getResult().getData().toString().equals("not_present"))
                        {

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    phoneNumber,                     // Phone number to verify
                                    60,                           // Timeout duration
                                    TimeUnit.SECONDS,                // Unit of timeout
                                    SignUp.this,        // Activity (for callback binding)
                                    mCallback);                      // OnVerificationStateChangedCallbacks


                            Toast.makeText(SignUp.this, phoneNumber, Toast.LENGTH_LONG).show();

                        }
                        else if(task.getResult().getData().toString().equals("present"))
                        {
                            Toast.makeText(SignUp.this,"this phone is already registered on another account",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(SignUp.this,"please try again",Toast.LENGTH_LONG).show();


                        }

                        return  null;
                    }
                });
    }






    public void onCountryPickerClick() {
        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
                  CountryCode=cpp.getSelectedCountryCodeWithPlus();

            }
        });


    }













}








