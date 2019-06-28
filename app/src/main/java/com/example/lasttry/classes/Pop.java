package com.example.lasttry.classes;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.EditText;

import com.example.lasttry.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Pop extends Activity {

    EditText e1;
    String groupId;
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     setContentView(R.layout.popup);
       DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

         e1=(EditText) findViewById(R.id.e1);

         uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        // updateGroup();

    }


   /*  //add group info
     public void updateGroup()
    {

        groupId= UUID.randomUUID().toString();
        String groupName=e1.getText().toString();
        //update user groups
        //update group for admin
        FirebaseDatabase.getInstance().getReference ("UserGroups")
                .child(uid).child("groups").child(groupId).child("Id").setValue(groupId);
        FirebaseDatabase.getInstance().getReference ("UserGroups")
                .child(uid).child("groups").child(groupId).child("name").setValue(groupName);



        //update for all other members
        for(String a:sel)
        {
            DatabaseReference r1=FirebaseDatabase.getInstance().getReference("UserGroups")
                    .child(a).child("groups").child(groupId);
            r1.child("Id").setValue(groupId);
            r1.child("name").setValue(groupName);

        }

        //update Groups
        FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("name").setValue(groupName);



        for(String a:sel)
        {
            FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("members")
                    .child(a).setValue(userSelected.get(sel.indexOf(a)));

        }

        FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("members")
                .child(uid).setValue(user_name);





    }*/



}
