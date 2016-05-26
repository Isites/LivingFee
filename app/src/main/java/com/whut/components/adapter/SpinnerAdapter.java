package com.whut.components.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.whut.view.R;

import java.util.List;

import cn.edu.whut.lib.common.DataResult;

public class SpinnerAdapter extends BaseAdapter{

    private List<DataResult> data;

    public SpinnerAdapter(){}
    public SpinnerAdapter(List<DataResult> data){
        this.data = data;
    }

    public void setData(List<DataResult> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        convertView = inflater.inflate(R.layout.item_spinner, null);
        if(convertView!=null) {
            TextView textView = (TextView) convertView.findViewById(R.id.record_spinner_item);
            textView.setText(data.get(position).getString("name"));
            //设置分类的id值
            convertView.setTag(data.get(position).getInt("id"));
        }
        return convertView;
    }
}
