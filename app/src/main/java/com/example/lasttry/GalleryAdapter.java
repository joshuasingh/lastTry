package com.example.lasttry;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private ArrayList<String> names;
    private Context mContext;
    RequestOptions options;
    Grid m1;



    public GalleryAdapter(ArrayList<String> gnames, Context mContext) {

         options= new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);


        names=new ArrayList<>();
        this.names = gnames;
        this.mContext = mContext;
        m1=(Grid) mContext;

    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate the cardview
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_card,viewGroup,false);
        ViewHolder vw=new ViewHolder(view);


        return vw;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(mContext).load(Uri.parse(names.get(i))).apply(options).into(viewHolder.i1);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }




    public  class ViewHolder extends  RecyclerView.ViewHolder
    {
        ImageView i1;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            i1=(ImageView) itemView.findViewById(R.id.i1);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                }
            });

        }

    }


}
