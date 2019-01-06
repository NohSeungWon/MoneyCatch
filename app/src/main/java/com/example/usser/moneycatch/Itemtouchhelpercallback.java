package com.example.usser.moneycatch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import javax.xml.transform.Source;

public class Itemtouchhelpercallback extends ItemTouchHelper.Callback {

    Itemtouchhelperlistner listener;

    public Itemtouchhelpercallback(Itemtouchhelperlistner listener){
        this.listener = listener;
    }


    @Override // 각 View 에서 어떤 user action이 가능한지 정의
    public int getMovementFlags( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder) {

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() { //드래그 작동여부
        return  false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() { // 스와이프 작동여부
        return true;
    }


    @Override  // user가 아이템을 drag 할 때 , itemtouch가 onmove를 호출
    public boolean onMove( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder,  RecyclerView.ViewHolder target) {

        return listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {
            listener.onItemRemove(viewHolder.getAdapterPosition());
    }
}
