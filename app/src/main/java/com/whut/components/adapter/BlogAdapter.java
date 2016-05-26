package com.whut.components.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whut.view.R;

import java.util.List;

import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.Utils;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder>{

    private List<DataResult> data;
    private IOnItemClicked itemListener;

    public BlogAdapter(){}
    public BlogAdapter(List<DataResult> data){
        this.data = data;
    }

    public void setData(List<DataResult> data) {
        this.data = data;
    }

    public void setItemListener(IOnItemClicked itemListener) {
        this.itemListener = itemListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_blog, parent, false
        );
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DataResult result = data.get(position);
        String date = result.getString("update_time");
        holder.blog_item.setText(result.getString("content"));
        holder.blog_date.setText(Utils.date2ShortStr(Utils.str2Date(date)));

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemListener != null){
                    itemListener.onClick(holder, position, result.getInt("id"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View rootView;

        public TextView blog_item;
        public TextView blog_date;


        public ViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
            init();
        }

        private void init(){
            blog_date = (TextView) rootView.findViewById(R.id.blog_date);
            blog_item = (TextView) rootView.findViewById(R.id.blog_item);
        }
    }
}
