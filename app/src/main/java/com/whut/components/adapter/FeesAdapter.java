package com.whut.components.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whut.view.R;

import java.util.Calendar;
import java.util.Date;

import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.DateUtils;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.fee.dao.FeeDao;
import cn.edu.whut.lib.fee.service.FeesService;
import cn.edu.whut.lib.user.dao.UserDao;

public class FeesAdapter extends RecyclerView.Adapter<FeesAdapter.ViewHolder>{

    private IOnItemClicked itemListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_fees, parent, false
        );
        ViewHolder holder = new ViewHolder(view);
        holder.imageView = (ImageView) view.findViewById(R.id.fees_img);
        holder.fees_context = (TextView) view.findViewById(R.id.fees_text);
        holder.fees_in = (TextView) view.findViewById(R.id.fees_in);
        holder.fees_out = (TextView) view.findViewById(R.id.fees_out);
        holder.fees_date = (TextView) view.findViewById(R.id.fees_date);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FeesService feesService = new FeesService();
        DataResult result = null;
        switch (position){
            case 0:
                Date now = new Date();
                String from = Utils.date2ShortStr(now) + " 00:00:00";
                String to = Utils.date2ShortStr(now) + " 23:59:59";
                holder.imageView.setImageResource(R.drawable.ic_today);
                holder.fees_context.setText(Utils.date2ShortStr(now));
                result = feesService.getFeesOfList(UserDao.DEFAULT_USER_ID, FeeDao.FEE_INCOME, from, to);
                holder.fees_in.setText(Utils.numLeft2(result.getFloat("totalFee")) + "");
                result = feesService.getFeesOfList(UserDao.DEFAULT_USER_ID, FeeDao.FEE_OUT, from, to);
                holder.fees_out.setText(Utils.numLeft2(result.getFloat("totalFee")) + "");
                break;
            case 1:
                holder.imageView.setImageResource(R.drawable.ic_week);
                holder.fees_date.setText("本周");
                Date monday = DateUtils.getFirstDayOfWeek();
                Date sunday = DateUtils.getLastDayOfWeek();
                holder.fees_context.setText(Utils.date2ShortStr(monday)+"~"+Utils.date2ShortStr(sunday));
                result = feesService.getFeesOfList(UserDao.DEFAULT_USER_ID, FeeDao.FEE_INCOME,
                        Utils.date2ShortStr(monday) + " 00:00:00", Utils.date2String(sunday)+ " 23:59:59");
                holder.fees_in.setText(Utils.numLeft2(result.getFloat("totalFee")) + "");
                result = feesService.getFeesOfList(UserDao.DEFAULT_USER_ID, FeeDao.FEE_OUT,
                        Utils.date2ShortStr(monday) + " 00:00:00", Utils.date2String(sunday)+ " 23:59:59");
                holder.fees_out.setText(Utils.numLeft2(result.getFloat("totalFee")) + "");
                break;
            case 2:
                holder.imageView.setImageResource(R.drawable.ic_month);
                holder.fees_date.setText("本月");
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH)+1;
                holder.fees_context.setText(month + "月");
                Date monthFrom = DateUtils.getFirstDayOfMonth();
                Date monthTo = DateUtils.getLastDayOfMonth();
                result = feesService.getFeesOfList(UserDao.DEFAULT_USER_ID, FeeDao.FEE_INCOME,
                        Utils.date2String(monthFrom), Utils.date2String(monthTo));
                holder.fees_in.setText(Utils.numLeft2(result.getFloat("totalFee")) + "");
                result = feesService.getFeesOfList(UserDao.DEFAULT_USER_ID, FeeDao.FEE_OUT,
                        Utils.date2String(monthFrom), Utils.date2String(monthTo));
                holder.fees_out.setText(Utils.numLeft2(result.getFloat("totalFee")) + "");
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemListener != null){
                    itemListener.onClick(holder, position, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setItemListener(IOnItemClicked itemListener) {
        this.itemListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView fees_context;
        public TextView fees_in;
        public TextView fees_out;
        public TextView fees_date;
        public View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

}
