package com.whut.components.adapter;

import android.support.v7.widget.RecyclerView;

public interface  IOnItemClicked {
    public void onClick(RecyclerView.ViewHolder holder, int position, Object tag);
}
