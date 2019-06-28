package com.example.lasttry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Otp extends AppCompatActivity {

    Button b1;
    EditText e1;
    String otp;
    FirebaseAuth mAuth;
    FirebaseAuth auth;
    DatabaseReference mDatabaseReference;
    public String verificationCode;
    public String Uname;
    String from;
    String phoneNumber;
    private FirebaseFunctions mFunctions;
    String refreshedToken;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

         b1=(Button) findViewById(R.id.b1);
         e1=(EditText) findViewById(R.id.e1);
         mAuth=FirebaseAuth.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("UserInfo");

        from=getIntent().getStringExtra("from");

        //initiallizing firebase function instance
        mFunctions = FirebaseFunctions.getInstance();

        if(from.equals("new"))
            Uname=getIntent().getStringExtra("Uname");
        verificationCode=getIntent().getStringExtra("otp");
        phoneNumber=getIntent().getStringExtra("num");
        SignIn();


    }

    private void SignIn() {
       b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               try {
                   otp = e1.getText().toString();
                   PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                   SigninWithPhone(credential);

               }
               catch (Exception e)
               {
                   Toast.makeText(Otp.this,e.getMessage(),Toast.LENGTH_LONG).show();
               }
           }
       });

    }



    private void SigninWithPhone(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                             refreshedToken = FirebaseInstanceId.getInstance().getToken();
                           if(from.equals("new")) {
                               updateUserInfo();

                           }
                           else
                           {
                               startActivity(new Intent(Otp.this,Groups.class));
                               finish();
                           }

                        }
                        else{
                            Toast.makeText(Otp.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }








    public void  updateUserInfo()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String uid=mAuth.getCurrentUser().getUid();
        mDatabaseReference.child(uid).child("name").setValue(Uname);
        mDatabaseReference.child(uid).child("id").setValue(mAuth.getCurrentUser().getUid());
        mDatabaseReference.child(uid).child("token").setValue(refreshedToken);
        mDatabaseReference.child(uid).child("phoneNo").setValue(phoneNumber);
        updateToken(refreshedToken);

    }



    //update the phone no
    private Task<ArrayList<String>> updateNo(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("list", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("updateNo")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down
                         Toast.makeText(Otp.this,task.getResult().getData().toString(),Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Otp.this,Groups.class));
                        finish();

                        Toast.makeText(Otp.this,"sign in",Toast.LENGTH_SHORT).show();
                         return  null;
                    }
                });
    }





    //update   the token
    private Task<ArrayList<String>> updateToken(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("list", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("updateToken")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, ArrayList<String>>() {
                    @Override
                    public ArrayList<String> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down
                        Toast.makeText(Otp.this,task.getResult().getData().toString(),Toast.LENGTH_LONG).show();
                        updateNo(phoneNumber);



                        return  null;
                    }
                });
    }





}
