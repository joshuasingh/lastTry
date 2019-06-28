package com.example.lasttry.classes;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.lasttry.NewGroupAdapter;
import com.example.lasttry.PhoneDirAdapter;

public class SwipeUpToRevert extends ItemTouchHelper.SimpleCallback{
    private NewGroupAdapter mAdapter;

    public SwipeUpToRevert(NewGroupAdapter adapter) {
        super(0, ItemTouchHelper.UP);
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction==ItemTouchHelper.UP)
        {
            mAdapter.revertItem(viewHolder.getAdapterPosition());
        }
    }
}
