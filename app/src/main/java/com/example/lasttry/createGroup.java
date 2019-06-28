package com.example.lasttry;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class createGroup extends AppCompatActivity {

    Button b1,b2;
    EditText e1;
    ListView l1,l2;
    ArrayList<String> list,sel;
    ArrayAdapter<String> a1,a2;
    DatabaseReference mref,mref1;
    String uid,groupId;
    ArrayList<String> userNames,userSelected;
    String user_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);


        b1 = (Button) findViewById(R.id.b1);
        b2=(Button) findViewById(R.id.b2);
        e1 = (EditText) findViewById(R.id.e1);
        l1 = (ListView) findViewById(R.id.l1);
        l2=(ListView) findViewById(R.id.l2);
        //id
        list = new ArrayList<>();
        sel = new ArrayList<>();

        //names
        userNames=new ArrayList<>();
        userSelected=new ArrayList<>();


        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        mref = FirebaseDatabase.getInstance().getReference("UserInfo");

        //getting list from database
        try {
            getListOfUser();
            initFCM();

        a1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        a2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userSelected);


        l1.setAdapter(a1);
        l2.setAdapter(a2);


        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(createGroup.this,list.get(i), Toast.LENGTH_LONG).show();

                    if (!sel.contains(list.get(i)))
                    {
                        sel.add(list.get(i));
                        userSelected.add(userNames.get(i));
                       }
                    else
                    {
                        sel.remove(list.get(i));
                        userSelected.remove(userNames.get(i));
                    }
                a2.notifyDataSetChanged();



            }
        });


        }
        catch (Exception e)
        {
            Toast.makeText(createGroup.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }




    private void sendRegistrationToServer(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserInfo");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("token")
                .setValue(token);
    }


    private void initFCM(){
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token);
    }









    public void getListOfUser()
    {
         mref.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for(DataSnapshot ds:dataSnapshot.getChildren())
                 {
                    String a=uid;
                    if(ds.child("id").getValue().toString()!=null) {
                        String b = ds.child("id").getValue().toString();

                        if (!a.equals(b)) {
                            list.add(ds.child("id").getValue().toString());
                            userNames.add(ds.child("name").getValue().toString());
                        } else {
                            user_name = ds.child("name").getValue().toString();
                        }
                    }
                    else
                    {
                        continue;
                    }

                 }



             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });




       //create group
         b1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 /*if(sel.isEmpty() ||e1.getText().toString().equals("")){
                     Toast.makeText(createGroup.this,"select members for group",Toast.LENGTH_LONG).show();
                 }
                 else
                 {
                     updateGroup();
                     Intent i = new Intent(createGroup.this, MainActivity.class);
                    i.putExtra("group_name", groupId);
                    i.putExtra("group_id",e1.getText().toString());
                     startActivity(i);

                 }*/

                 Intent i =new Intent(createGroup.this,CreateNewGroup.class);
                 startActivity(i);

             }
             });



          b2.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  Intent i = new Intent(createGroup.this, Groups.class);


                  startActivity(i);
              }
          });





         }


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





    }


}
