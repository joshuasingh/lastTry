package com.example.lasttry.classes;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.example.lasttry.PhoneDirAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private PhoneDirAdapter mAdapter;

    public SwipeToDeleteCallback(PhoneDirAdapter adapter) {
        super(0, ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
           if(direction==ItemTouchHelper.LEFT)
           {
               String a="sdfsdf";
           }
           else{
                 mAdapter.deleteItem(viewHolder.getAdapterPosition());
           }
    }
}