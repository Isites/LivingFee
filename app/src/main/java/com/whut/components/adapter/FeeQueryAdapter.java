package com.whut.components.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whut.view.R;


import java.util.Calendar;
import java.util.List;

import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.fee.dao.FeeDao;

public class FeeQueryAdapter extends RecyclerView.Adapter<FeeQueryAdapter.ViewHolder>{

    public static final String[] WEEK_STR = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    private IOnItemClicked itemListener;
    private List<DataResult> data;

    public FeeQueryAdapter(){}
    public FeeQueryAdapter(List<DataResult> data){
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
                R.layout.item_fees_history, parent, false
        );
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataResult item = data.get(position);
        Calendar cal = Calendar.getInstance();
        cal.setTime(Utils.str2Date(item.getString("update_time")));
        holder.history_day.setText(cal.get(Calendar.DAY_OF_MONTH) + "");
        holder.history_week.setText(WEEK_STR[cal.get(Calendar.DAY_OF_WEEK) - 1 ]);
        holder.history_img.setImageResource(item.getInt("resource"));
        holder.history_fee.setText(item.getFloat("fee") + "");
        holder.history_name.setText(item.getString("name"));
        int type = item.getInt("type");
        if(type == FeeDao.FEE_INCOME){
            holder.history_fee.setTextColor(Color.parseColor("#FF4081"));
        }
        else if(type == FeeDao.FEE_OUT){
            holder.history_fee.setTextColor(Color.parseColor("#33AE54"));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public View rootView;

        public TextView history_day;
        public TextView history_week;
        public TextView history_name;
        public TextView history_fee;

        public ImageView history_img;

        public ViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
            init(itemView);
        }

        private void init(View parent){
            history_day = (TextView) parent.findViewById(R.id.history_day);
            history_week = (TextView) parent.findViewById(R.id.history_week);
            history_name = (TextView) parent.findViewById(R.id.history_name);
            history_fee = (TextView) parent.findViewById(R.id.history_fee);
            history_img = (ImageView) parent.findViewById(R.id.history_img);
        }
    }
}
