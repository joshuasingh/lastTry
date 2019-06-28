package com.example.lasttry;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PhoneDirAdapter extends RecyclerView.Adapter<PhoneDirAdapter.ViewHolder> {

    private ArrayList<String> a1,a2;
    Context ctx;
    CreateNewGroup c1;


    public PhoneDirAdapter(ArrayList<String> a,ArrayList<String> b,Context ctx1)
    {
        a1=a;
        a2=b;
        ctx=ctx1;
        c1=(CreateNewGroup) ctx;
    }



    @Override
    public PhoneDirAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.calling_cardview,parent,false);
        PhoneDirAdapter.ViewHolder vw=new PhoneDirAdapter.ViewHolder(view);


        return vw;
    }

    @Override
    public void onBindViewHolder(PhoneDirAdapter.ViewHolder holder, int position) {
                    holder.t1.setText(a1.get(position));
                    holder.t2.setText(a2.get(position));
    }

    @Override
    public int getItemCount() {
        return a1.size();
    }



    public void deleteItem(int position) {
       String b=a1.get(position);
       String a=a2.get(position);
        a1.remove(position);
       a2.remove(position);
       notifyDataSetChanged();
       c1.addDataToGroup(b,a);

    }


   public class ViewHolder extends RecyclerView.ViewHolder
   {
       TextView t1,t2;

       public ViewHolder(View itemView) {
           super(itemView);
           t1=(TextView) itemView.findViewById(R.id.t1);
           t2=(TextView) itemView.findViewById(R.id.t2);

       }


   }







}
