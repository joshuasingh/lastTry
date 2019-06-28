package com.example.lasttry;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdaper extends RecyclerView.Adapter<RecyclerAdaper.ViewHolder> {

    private ArrayList<String> names;
    private Context mContext;
    private View selected;
    ArrayAdapter<String> a1;
    Grid m1;



    public RecyclerAdaper(ArrayList<String> gnames, Context mContext) {
        selected=null;
        names=new ArrayList<>();
        this.names = gnames;
        this.mContext = mContext;
        m1=(Grid) mContext;

    }

    @NonNull
    @Override
    public RecyclerAdaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       //inflate the cardview
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle,viewGroup,false);
        ViewHolder vw=new ViewHolder(view);


        return vw;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
             viewHolder.t1.setText(names.get(i));

    }

    @Override
    public int getItemCount() {
        return names.size();
    }




    public  class ViewHolder extends  RecyclerView.ViewHolder
    {
        TextView t1;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            t1=(TextView) itemView.findViewById(R.id.t1);

           /* itemView.setOnClickListener(new View.OnFocusChangeListener()
            {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        // run scale animation and make it bigger
                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_in_tv);
                        itemView.startAnimation(anim);
                        anim.setFillAfter(true);
                    } else {
                        // run scale animation and make it smaller
                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_out_tv);
                        itemView.startAnimation(anim);
                        anim.setFillAfter(true);
                    }
                }
             });
            */

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   if(selected!=null) {
                       //put last one down
                       Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_out_tv);
                       selected.startAnimation(anim);
                       anim.setFillAfter(true);


                   }
                    m1.clicked(t1.getText().toString());

                       //increase this one
                       Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_in_tv);
                       itemView.startAnimation(anim);
                       anim.setFillAfter(true);
                       selected = itemView;









                }
            });

        }

    }


}
