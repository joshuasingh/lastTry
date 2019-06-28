package com.example.lasttry;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lasttry.Services.MyFirebaseInstanceService;
import com.example.lasttry.classes.AppDatabase;
import com.example.lasttry.classes.PIcId;
import com.example.lasttry.classes.UserDao;
import com.example.lasttry.interfaces.AsyncResponse;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Grid extends AppCompatActivity implements AsyncResponse{

     GridView androidGridView;
     DatabaseReference mDRef;
     Bundle extras;
     String newString;
     FirebaseAuth mAuth;
     String id;
     private static final String TAG = "MyActivity";
     private ArrayList<String> imageIDs;
     private ArrayList<String> offlineIDs;
     DatabaseReference mRef,mref1;
    private final int PICK_IMAGE_REQUEST = 71;
     String group_id;
    RecyclerView r1,r11;
    RecyclerAdaper r2;
    GalleryAdapter r22;
    private ArrayList<String> groupMember;
    HashMap<String,ArrayList<String>> h1;
    int first=1;
    private BroadcastReceiver myReceiver;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        imageIDs=new ArrayList<>();
        offlineIDs=new ArrayList<>();
        group_id= getIntent().getStringExtra("group_name");
        groupMember=new ArrayList<>( (ArrayList<String>) getIntent().getSerializableExtra("group_member"));
        h1=new HashMap<>();
        currentUser="all";


        //BROADCAST Recciver
         BroadcastReceiver myReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String msg=intent.getStringExtra("url");
                String[] a=msg.split(",");
                insertNewVals(a);
              Toast.makeText(Grid.this,a[0]+" "+a[1],Toast.LENGTH_LONG).show();

            }
        };





        //REGISTER the Broadcast RECEIVER
        registerReceiver(myReceiver, new IntentFilter(MyFirebaseInstanceService.INTENT_FILTER));









         //setting recycler View
        try {
            r1 = findViewById(R.id.rec1);
            r11=findViewById(R.id.r1);
            ArrayList<String> a1 = new ArrayList<>();
            a1.add("sdd");
            a1.add("sd");
            a1.add("swei");
            r2 = new RecyclerAdaper(groupMember, this);
            r1.setAdapter(r2);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(0);
            r1.setLayoutManager(layoutManager);
            String a = "sdfsdf";

            //initialize the listview


        }
        catch (Exception e)
        {
            Toast.makeText(Grid.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }







        mRef=FirebaseDatabase.getInstance().getReference("UserGroups")
                .child(id).child("groups")
                .child(group_id).child("photos");



        mRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int value=0;
                String updated_key=null;
                String updated_Value=null;

               //for the first time
               if (first == 1) {
                   first = 0;
                   for (DataSnapshot ds : dataSnapshot.getChildren()) {
                       insertVals(ds);
                   }


                   r22 = new GalleryAdapter(imageIDs, Grid.this);
                   r11.setAdapter(r22);
                   final LinearLayoutManager layoutManager = new GridLayoutManager(Grid.this, 4);
                   layoutManager.setOrientation(0);
                   r11.setLayoutManager(layoutManager);

               }





              }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void insertNewVals(String[] a)
    {

       if(currentUser.equals(a[1])) {
           imageIDs.add(a[0]);
       }

        if(!h1.containsKey(a[1]))
        {
            h1.put(a[1],new ArrayList<String>(Arrays.asList(a[0])));


        }
        else
        {
            try {

                String b=a[0];
                h1.get(a[1]).add(b);

            }catch(Exception e)
            {

                Toast.makeText(Grid.this,e.getMessage(),Toast.LENGTH_LONG).show();

            }


        }


       r22.notifyItemInserted(imageIDs.size());


    }




    public void insertVals(DataSnapshot ds)
    {

        if (ds.child("url").getValue().toString() != null) {
            if (!imageIDs.contains(ds.child("url").getValue().toString())) {
                imageIDs.add(ds.child("url").getValue().toString());
            }



            if(!h1.containsKey(ds.child("uploadedby").getValue().toString()))
            {
                h1.put(ds.child("uploadedby").getValue().toString(),new ArrayList<String>(Arrays.asList(ds.child("url").getValue().toString())));

            }
            else
            {
                try {
                   ;
                    String b=ds.child("url").getValue().toString();
                    h1.get(ds.child("uploadedby").getValue().toString()).add(b);

                }catch(Exception e)
                {

                    Toast.makeText(Grid.this,e.getMessage(),Toast.LENGTH_LONG).show();

                }


            }

        }


    }






    @Override
    public void processFinish(ArrayList<String> output)
    {
        if(extras == null)
        {
            newString= null;
        }
        else
        {            /* fetching the string passed with intent using ‘extras’*/

            newString= extras.getString("url");
            String g="hgjjj";

        }


    }







    //static class to upload  pics
    private static class AgentAsyncTask extends AsyncTask<Void, Void,ArrayList<String>>
    {

       public AsyncResponse delegate = null;
       Context ctx;
       ArrayList<String> imgIds;

        public AgentAsyncTask(AsyncResponse delegate,Context ctx)
        {
            this.delegate = delegate;
            this.ctx=ctx;
            this.imgIds=new ArrayList<>();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            AppDatabase db = Room.databaseBuilder(ctx,
                    AppDatabase.class, "database-name").build();
            try
            {
                UserDao d1 = db.userDao();
                List<PIcId> l1=d1.getAll();
                for(PIcId p:l1)
                {
                    imgIds.add(p.getUrl());

                }

            }catch (Exception e)
            {
                Log.v(TAG, e.getMessage()+"fine");

            }


            return imgIds;
        }

        @Override
        protected void onPostExecute(ArrayList<String> a1)
        {
                  delegate.processFinish(a1);

        }
    }




    public void clicked(String a)
    {
         currentUser=a;
        imageIDs.clear();
        try
        {
            imageIDs.addAll(h1.get(a));
            r22.notifyDataSetChanged();
            Toast.makeText(Grid.this,a+String.valueOf(h1.get(a).size()),Toast.LENGTH_LONG).show();


        }catch (Exception e)
        {
            Toast.makeText(Grid.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }



    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);


    }





}
