package com.example.lasttry;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.annotations.CheckReturnValue;

public class NewGroupAdapter extends RecyclerView.Adapter<NewGroupAdapter.ViewHolder>  {

    ArrayList<String> a1;
    CreateNewGroup ctx;

    public NewGroupAdapter(ArrayList<String> a2, Context ctx1)
    { a1=new ArrayList<>();
       a1=a2;

       ctx=(CreateNewGroup) ctx1;

    }


    @Override
    public NewGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.new_group_cardview,parent,false);
        NewGroupAdapter.ViewHolder vw=new NewGroupAdapter.ViewHolder(view);


        return vw;
    }

    @Override
    public void onBindViewHolder(NewGroupAdapter.ViewHolder holder, int position) {
              holder.t1.setText(a1.get(position));
       // Toast.makeText(ctx,"here"+ String.valueOf(a1.size()),Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() { 
        return a1.size();
    }


    public void revertItem(int position)
    {
        String a=a1.get(position);
         a1.remove(position);
         notifyDataSetChanged();
         ctx.giveBack(a,position);




    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
         TextView t1;
        public ViewHolder(View itemView) {
            super(itemView);
          t1=(TextView) itemView.findViewById(R.id.t1);
         }
    }


}
