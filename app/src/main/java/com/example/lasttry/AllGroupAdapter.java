package com.example.lasttry;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import javax.crypto.Cipher;

public class AllGroupAdapter extends RecyclerView.Adapter<AllGroupAdapter.ViewHolder>{

    ArrayList<String> a;
   Context ctx;

    public AllGroupAdapter(ArrayList<String> a, Context ctx) {
        this.a = a;
        this.ctx=ctx;
    }

    @Override
    public AllGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_groups,parent,false);
        AllGroupAdapter.ViewHolder vw=new AllGroupAdapter.ViewHolder(view);


        return vw;
    }

    @Override
    public void onBindViewHolder(AllGroupAdapter.ViewHolder holder, int position) {
                 holder.t1.setText(a.get(position));
    }

    @Override
    public int getItemCount() {
        return a.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView t1;
        public ViewHolder(View itemView) {
            super(itemView);
            t1=(TextView) itemView.findViewById(R.id.group_name);
        }
    }


}

