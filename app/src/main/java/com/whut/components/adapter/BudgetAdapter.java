package com.whut.components.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whut.view.R;

import java.util.List;

import cn.edu.whut.lib.common.DataResult;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder>{

    private IOnItemClicked itemListener;
    private List<DataResult> data;

    public BudgetAdapter(){}
    public BudgetAdapter(List<DataResult> data){
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
                R.layout.item_budget, parent, false
        );
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DataResult result = data.get(position);

        holder.budget_img.setImageResource(result.getInt("resource"));
        holder.budget_name.setText(result.getString("name"));

        float budget = result.getFloat("fee", 0f);
        holder.budget_fee.setText(budget + "");

        float totalOut = result.getFloat("totalOut", 0f);
        holder.budget_left.setText((budget - totalOut) + "");
        if(totalOut >= budget){
            totalOut = budget;
        }

        holder.budget_progress.setProgress((int) (100 * totalOut / budget));

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

        public TextView budget_left;
        public TextView budget_name;
        public TextView budget_fee;
        public ProgressBar budget_progress;
        public ImageView budget_img;

        public ViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
            init();
        }

        private void init(){

            budget_fee = (TextView) rootView.findViewById(R.id.budget_fee);
            budget_name = (TextView) rootView.findViewById(R.id.budget_name);
            budget_left = (TextView) rootView.findViewById(R.id.budget_left);

            budget_img = (ImageView) rootView.findViewById(R.id.budget_img);
            budget_progress = (ProgressBar) rootView.findViewById(R.id.budget_progress);
        }
    }
}
