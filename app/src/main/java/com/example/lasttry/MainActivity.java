package com.example.lasttry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasttry.classes.AppDatabase;
import com.example.lasttry.classes.PIcId;
import com.example.lasttry.classes.UserDao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button btnChoose, btnUpload,btnShow;
    private ImageView imageView;

    private Uri filePath,temp1;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDatabaseReference,mref1;
    String temp;
    String Id;
    FirebaseAuth mAuth;
    String groupName,groupId,picId;
    private final int PICK_IMAGE_REQUEST = 71;
    Upload up1;
    Photo pic1;
    private static final String TAG = "MyActivity";
    Intent i;
    ArrayList<String> group_member;
    HashMap<String,String> h1;
    ListView l1;
    ArrayAdapter<String> a1;
    TextView t1;
    private static final  String GROUP_NAME="group name";
    private static final  String GROUP_LIST="group list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        mAuth=FirebaseAuth.getInstance();
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgView);
        btnShow=(Button) findViewById(R.id.btnShow);
        h1=new HashMap<>();
        l1=(ListView) findViewById(R.id.l1);
        t1=(TextView) findViewById(R.id.e1);

        i=getIntent();

        //getting values from saved instance

        if(savedInstanceState!=null) {
            group_member=new ArrayList<>(savedInstanceState.getStringArrayList(GROUP_LIST));
            groupName=savedInstanceState.getString(GROUP_NAME);
        }
        else
        {

            groupId = i.getStringExtra("group_name");
            groupName = i.getStringExtra("group_id");
            t1.setText(groupName);
            uploadValues();
            group_member = new ArrayList<>();
        }




         //setting the list
        a1=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,group_member);
        l1.setAdapter(a1);







        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

         btnShow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 showNew();
             }
         });




    }


     //saving values in saved instances


    @Override
    protected void onResume() {
        String a="sdfdsf";
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(GROUP_NAME,groupName);
        outState.putStringArrayList(GROUP_LIST,group_member);
        super.onSaveInstanceState(outState);
    }

    private void uploadValues()
     {
         //get group members
         mDatabaseReference= FirebaseDatabase.getInstance().getReference("Groups").child(groupId).child("members");

         mDatabaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for(DataSnapshot ds:dataSnapshot.getChildren())
                 {
                     h1.put(ds.getKey().toString(),ds.getValue().toString());
                     group_member.add(ds.getValue().toString());

                 }
                 a1.notifyDataSetChanged();

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });





     }








    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    private void showNew()
    {
        Intent i=new Intent(MainActivity.this,Grid.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String a=groupId;
        i.putExtra("group_name",groupId);
        i.putExtra("group_member",group_member);
         startActivity(i);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            temp1=data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {


        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            final Upload[] upload = new Upload[1];
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ System.currentTimeMillis());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // getting image uri and converting into string
                                     Uri downloadUrl = uri;
                                     Toast.makeText(MainActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
                                        temp=uri.toString();
                                    pic1=new Photo("1",temp1.toString());
                                    up1=new Upload("newName",pic1);
                                    picId=UUID.randomUUID().toString();

                                 try {
                                   DatabaseReference mref= FirebaseDatabase.getInstance().getReference("UserGroups")
                                             .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("groups").child(groupId)
                                             .child("photos").child(picId);
                                              mref.child("uploadedby").setValue(h1.get(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                                               mref.child("url").setValue(temp);




                                     //upload pic in others users

                                      DatabaseReference mref1;
                                     for (String a : new ArrayList<>(h1.keySet())) {
                                         if (!a.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                             mref1=FirebaseDatabase.getInstance().getReference("UserGroups")
                                                     .child(a).child("groups").child(groupId)
                                                     .child("photos").child(picId);
                                             mref1.child("url").setValue(temp);
                                             mref1.child("uploadedby").setValue(h1.get(FirebaseAuth.getInstance().getCurrentUser().getUid()));

                                         }
                                     }

                                 }
                                 catch (Exception e)
                                 {
                                     Toast.makeText(MainActivity.this, String.valueOf(group_member.size()), Toast.LENGTH_SHORT).show();

                                 }





                                }


                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploading "+(int)progress+"%");
                        }
                    });




        }
    }


     //static class to upload  pics
    /* private static class AgentAsyncTask extends AsyncTask<Void, Void, Integer> {

         //Prevent leak
         private String id;
         private String group;
         private  String url;
         private Context cnx;

         public AgentAsyncTask(String id, String group, String url, Context cnx1)
         {
             this.id=id;
             this.group = group;
             this.url = url;
             this.cnx=cnx1;
         }

         @Override
         protected Integer doInBackground(Void... params) {

             AppDatabase db = Room.databaseBuilder(cnx,
                     AppDatabase.class, "database-name").build();
             try
             {
                  UserDao d1 = db.userDao();
                 PIcId p1 = new PIcId(this.id,this.group, this.url);
                 d1.insertAll(p1);
                 List<PIcId> l1=d1.getAll();
                 for(PIcId p:l1)
                 {
                     Log.v(TAG,p.toString());
                 }

             }catch (Exception e)
             {
                 Log.v(TAG, e.getMessage()+"fine");

             }








             return 1;
         }

         @Override
         protected void onPostExecute(Integer agentsCount) {

         }
     }
        */











}
