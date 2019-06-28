package com.example.lasttry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.lang.reflect.Array;
import java.nio.BufferUnderflowException;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Groups extends AppCompatActivity {


    RecyclerView r1;
    DatabaseReference mref;
    String uid;
    ArrayList<String> groupName,groupId;
    AllGroupAdapter a1;
    private FirebaseFunctions mFunctions;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        b1=(Button) findViewById(R.id.b1);
        try
        {
       // uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
         uid="8vuRcQ0KZMOTHIo6FdGsx4oxKiU2";
            groupName=new ArrayList<>();
        groupId=new ArrayList<>();



            //initiallizing firebase function instance
            mFunctions = FirebaseFunctions.getInstance();


        //initialize recycler view
        r1=findViewById(R.id.all_groups);




            a1=new AllGroupAdapter(groupName,Groups.this);
            r1.setAdapter(a1);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(1);
            r1.setLayoutManager(layoutManager);



         b1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 getGroups(uid);
                 Toast.makeText(Groups.this,"called",Toast.LENGTH_LONG).show();
             }
         });


        }
        catch (Exception e)
        {
            Toast.makeText(Groups.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }


        //add listener to groups
        // clickActionList();
    }


/*
    public void clickActionList()
    {
        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent k=new Intent(Groups.this,MainActivity.class);
                k.putExtra("group_id",list.get(i));
                k.putExtra("group_name",list1.get(i));
                startActivity(k);
            }
        });

    }

  */



    //getting all groups of user
    private Task<ArrayList<String>> getGroups(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("userId", text);
        data.put("push", true);

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
                                  groupName.add(a1[0]);
                                  groupId.add(a1[1]);

                               Toast.makeText(Groups.this,a, Toast.LENGTH_LONG).show();

                           }
                           a1.notifyDataSetChanged();
                       }catch(Exception e) {
                           Toast.makeText(Groups.this,e.getMessage(), Toast.LENGTH_LONG).show();
                       }

                        return  null;
                    }
                });
    }








    public void getListOfGroups()
    {




    }


}