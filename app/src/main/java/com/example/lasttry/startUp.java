package com.example.lasttry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.hbb20.CountryCodePicker;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class startUp extends AppCompatActivity {

    EditText t1,t2;
    Button b1,b2;
    FirebaseAuth mAuth;
    CountryCodePicker cpp;
    DatabaseReference mDatabaseReference;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    String CountryCode;
    String verificationCode,phoneNumber;
    FirebaseAuth auth;
    private FirebaseFunctions mFunctions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);


        //get read permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        }


        //initiallizing firebase function instance
        mFunctions = FirebaseFunctions.getInstance();


        //set the title
        t1=(EditText) findViewById(R.id.t1);
        b1=(Button) findViewById(R.id.b1);
        b2=(Button) findViewById(R.id.b2);

        cpp= findViewById(R.id.ccp);
        mAuth=FirebaseAuth.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("UserInfo");
        StartFirebaseLogin();
        onCountryPickerClick();


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = CountryCode;
                phoneNumber = temp.concat(t1.getText().toString());

                checkExistence(phoneNumber);




            }





        });



        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //signUp();
             //  startActivity(new Intent(startUp.this,SignUp.class));
                startActivity(new Intent(startUp.this, Groups.class));

                finish();

               // getGroups("8vuRcQ0KZMOTHIo6FdGsx4oxKiU2");
            }
        });



    }



    //check if the account exist
        private Task<ArrayList<String>> checkExistence(String text) {
            // Create the arguments to the callable function.
            Map<String, Object> data = new HashMap<>();
            data.put("list", text);
            data.put("push", true);

            return mFunctions
                    .getHttpsCallable("checkNo")
                    .call(data)
                    .continueWith(new Continuation<HttpsCallableResult, ArrayList<String>>() {
                        @Override
                        public ArrayList<String> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                            // This continuation runs on either success or failure, but if the task
                            // has failed then getResult() will throw an Exception which will be
                            // propagated down.

                           String res1=task.getResult().getData().toString();
                          if(res1.equals("present")) {
                              PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                      phoneNumber,                     // Phone number to verify
                                      60,                           // Timeout duration
                                      TimeUnit.SECONDS,                // Unit of timeout
                                      startUp.this,        // Activity (for callback binding)
                                      mCallback);                      // OnVerificationStateChangedCallbacks


                              Toast.makeText(startUp.this, phoneNumber, Toast.LENGTH_LONG).show();
                          }
                            else if(res1.equals("not_present"))
                          {
                              Toast.makeText(startUp.this, "account not present ,first register", Toast.LENGTH_LONG).show();

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
                CountryCode = cpp.getSelectedCountryCodeWithPlus();

            }
        });


    }




    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(startUp.this,"verification completed",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(startUp.this,"not able to verify",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Intent i=new Intent(startUp.this,Otp.class);
                i.putExtra("from","old");
                i.putExtra("otp",verificationCode);
                i.putExtra("num",phoneNumber);
                Toast.makeText(startUp.this,"Code sent",Toast.LENGTH_SHORT).show();

                startActivity(i);
                finish();
            }
        };
    }





    //getting all groups of user
    private Task<ArrayList<String>> getGroups(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("userId", text);
        data.put("push", true);
        Toast.makeText(startUp.this,"called", Toast.LENGTH_LONG).show();


        return mFunctions
                .getHttpsCallable("getUserGroups")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down
                        try {
                            for(String a: (ArrayList<String>)task.getResult().getData())
                            {
                                 String[] a1=a.split("/");

                                Toast.makeText(startUp.this,a1[0]+ "  "+a1[1], Toast.LENGTH_LONG).show();

                            }
                        }catch(Exception e) {
                            Toast.makeText(startUp.this,e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        return  null;
                    }
                });
    }








}
