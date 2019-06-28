package com.example.lasttry;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasttry.classes.DatabaseHepler;
import com.example.lasttry.classes.Pop;
import com.example.lasttry.classes.SwipeToDeleteCallback;
import com.example.lasttry.classes.SwipeUpToRevert;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.BufferUnderflowException;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateNewGroup extends AppCompatActivity {


    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    RecyclerView r1,r2;
    PhoneDirAdapter p1;
    NewGroupAdapter p2;
    HashMap<String,String> nameContacts;
    ArrayList<String> nameSelectedExtra;
    ArrayList<String> name1,number1,nameSelected;
    private FirebaseFunctions mFunctions;
    DatabaseHepler myDB;
    TextView t1;
    Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);

        //initiallizing firebase function instance
        mFunctions = FirebaseFunctions.getInstance();

        t1=(TextView) findViewById(R.id.t1);
        b1=(Button) findViewById(R.id.b1);
        b1.setEnabled(false);


        //getting all the contacts
        try {

            nameContacts=new HashMap<>();
           //not the first time
            if(getSharedValue()==1) {

                getFromFile();
                name1 = new ArrayList<>(nameContacts.values());
                number1 = new ArrayList<>(nameContacts.keySet());

            }//first time
             else
            {
                showContacts();
                name1 = new ArrayList<>(nameContacts.values());
                number1 = new ArrayList<>(nameContacts.keySet());
                addMessage(number1);
                name1 = new ArrayList<>(nameContacts.values());
                number1 = new ArrayList<>(nameContacts.keySet());

            }

            r1=(RecyclerView) findViewById(R.id.r1);
            p1=new PhoneDirAdapter(name1,number1,CreateNewGroup.this);
            r1.setAdapter(p1);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(1);
            r1.setLayoutManager(layoutManager);
            attachEvent();

            //initialize the horizontal recycler view
            initializeRecyclerView();

        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        initiateCreation();

    }



    //on Click listener of group create button
    public void initiateCreation()
    {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CreateNewGroup.this, Pop.class));

            }
        });

    }



    //initialize the horizontal recycler view
    private void initializeRecyclerView() {
        nameSelected=new ArrayList<>();
        nameSelectedExtra=new ArrayList<>();


        r2=(RecyclerView) findViewById(R.id.r2);
        p2=new NewGroupAdapter(nameSelected,this);
        r2.setAdapter(p2);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(0);
        r2.setLayoutManager(layoutManager);
        if(nameSelected.isEmpty())
        {
            t1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.INVISIBLE);
        }
        else
        {
            r2.setVisibility(View.VISIBLE);
            t1.setVisibility(View.INVISIBLE);
        }

        attachEvent1();

    }


    //attach itemtouch helper to attach swipe recognization
    private void attachEvent()
    {
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(p1));
        itemTouchHelper.attachToRecyclerView(r1);


    }


    //attach itemtouch helper to attach swipe up on horizontal recyc
    private void attachEvent1()
    {
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeUpToRevert(p2));
        itemTouchHelper.attachToRecyclerView(r2);


    }



    private int getSharedValue()
    {
        Context context =this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sh_file_name), Context.MODE_PRIVATE);
        int highScore = sharedPref.getInt("UpdateContactList",0);
        return  highScore;
    }


    private void putSharedValue()
    {
        //using shared preferences
        Context context =(Activity)this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.sh_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("UpdateContactList", 1);
        editor.apply();
    }



    //add data to horizontal recycler view
    public void addDataToGroup(String a,String b)
    {

          //enabling and disabling
           if(nameSelectedExtra.size()==0)
           {
               b1.setEnabled(true);
           }

          nameSelected.add(a);
          nameSelectedExtra.add(b);

          Toast.makeText(CreateNewGroup.this,"position"+String.valueOf(nameSelectedExtra.size()),Toast.LENGTH_LONG).show();

          p2.notifyDataSetChanged();

          //setVisibilty to horizontal recycler view

         r2.setVisibility(View.VISIBLE);
         t1.setVisibility(View.INVISIBLE);



        //starting group activities
        //  startActivity(new Intent(CreateNewGroup.this,InsideGroup.class));
        //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_left);

    }


    public void giveBack(String a,Integer pos)
    {

        if(nameSelectedExtra.size()==1)
        {
            b1.setEnabled(false);
        }
        String num=nameSelectedExtra.get(pos);
        nameSelectedExtra.remove(num);

        //update main recycler view
        {
            name1.add(a);
            number1.add(num);
        }
        p1.notifyDataSetChanged();
    }


    private void addInFile()
    {

      myDB=new DatabaseHepler(this);



      for(Map.Entry<String,String> k:nameContacts.entrySet())
      {
         try {
             myDB.insertData(k.getKey(), k.getValue());
         }catch (Exception e) {
             Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
         }


      }
    }


    /*
    private HashMap<String,String> getFromFile()
    {

        return h1;
    }
     */

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
              getContactNames();

        }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }



    //make th ephone number right format
    private  String formattedNumber(String a)
    {
        String b="";
        Pattern pt = Pattern.compile("[^0-9]");
        Matcher match= pt.matcher(a);

            a=a.replaceAll("-", "").replaceAll(" ","");

            if(a.length()>10)
            {
                b=a.substring(a.length()-10);
            }
            else
            {
                b=a;
            }


        return b;
    }






    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private HashSet<String> getContactNames() {
        HashSet<String> contacts=new HashSet<>();
        // Get the ContentResolver
        ContentResolver cr = getContentResolver();
        // Get the Cursor of all the contacts
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        // Move the cursor to first. Also check whether the cursor is empty or not.
        if (cursor.moveToFirst()) {
            // Iterate through the cursor
            while(cursor.moveToNext()){
                // Get the contacts name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String Number= formattedNumber(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                 nameContacts.put(Number,name);

                    }
        }
        // Close the curosor
         cursor.close();
         putSharedValue();
        return contacts;
    }



   //call the cloud function
   private Task<ArrayList<String>> addMessage(ArrayList<String> text) {
       // Create the arguments to the callable function.
       Map<String, Object> data = new HashMap<>();
       data.put("list", text);
       data.put("push", true);

       return mFunctions
               .getHttpsCallable("addMessage")
               .call(data)
               .continueWith(new Continuation<HttpsCallableResult, ArrayList<String>>() {
                   @Override
                   public ArrayList<String> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                       // This continuation runs on either success or failure, but if the task
                       // has failed then getResult() will throw an Exception which will be
                       // propagated down.
                        number1.clear();
                        name1.clear();
                        for(String i:(ArrayList<String>) task.getResult().getData()) {
                            number1.add(i);
                            name1.add(nameContacts.get(i));
                        }

                       //update the hashmap
                        nameContacts.clear();
                        int j=0;
                       for(String i:number1) {

                         nameContacts.put(i,name1.get(j));
                         j++;
                       }

                       try
                       {
                           p1.notifyDataSetChanged();
                           addInFile();
                       }
                       catch (Exception e)
                       {
                           Toast.makeText(CreateNewGroup.this,e.getMessage(),Toast.LENGTH_LONG).show();
                       }
                       return (ArrayList<String>) task.getResult().getData();

                   }
               });
   }



   //reading from database
    public void getFromFile()
    {
         myDB=new DatabaseHepler(this);
     Cursor res=myDB.getAllData();
      if(res.getCount()==0)
      {
          Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
      }
      else
      {
          nameContacts.clear();
          while(res.moveToNext())
          {
              nameContacts.put(res.getString(1),res.getString(2));

          }
      }
    }


}
