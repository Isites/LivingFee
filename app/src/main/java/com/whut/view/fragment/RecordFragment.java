package com.whut.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.rebound.ui.Util;
import com.whut.components.adapter.SpinnerAdapter;
import com.whut.view.R;
import com.zcw.togglebutton.ToggleButton;

import java.util.List;

import cn.edu.whut.lib.common.Dao;
import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.fee.dao.FeeDao;
import cn.edu.whut.lib.spendType.dao.SpendTypeDao;
import cn.edu.whut.lib.spendType.service.SpendTypeService;
import cn.edu.whut.lib.user.dao.UserDao;

public class RecordFragment extends Fragment{

    private AppCompatActivity activity;

    private TextView record_type;
    private EditText record_fee;
    private ToggleButton record_toggle;
    private EditText record_desc;
    private AppCompatSpinner record_category_1;
    private AppCompatSpinner record_category_2;
    private SpinnerAdapter category1;
    private SpinnerAdapter category2;
    private Button record_save;
    //选中后的spend_type id
    private int category_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fees_recorder_fragment, container, false);
        init(fragment);
        return fragment;
    }


    private void init(View view){
        initView(view);
        initData();
        initListener();
    }
    private void initView(View rootView){
        record_fee = (EditText) rootView.findViewById(R.id.record_fee);
        record_toggle = (ToggleButton) rootView.findViewById(R.id.record_toggle);
        record_type = (TextView) rootView.findViewById(R.id.record_type);
        record_category_1 = (AppCompatSpinner) rootView.findViewById(R.id.record_category_1);
        record_category_2 = (AppCompatSpinner) rootView.findViewById(R.id.record_category_2);
        record_save = (Button) rootView.findViewById(R.id.record_save);
        record_desc = (EditText) rootView.findViewById(R.id.record_desc);

        category1 = new SpinnerAdapter();
        category2 = new SpinnerAdapter();
    }
    private void initData(){
        if(record_toggle.isToggleOn()){
            initSpinner(SpendTypeDao.TYPE_OUT);
        }
        else {
            initSpinner(SpendTypeDao.TYPE_IN);
        }
    }
    private void initSpinner(int type){
        //true 表示支出
        setSpinnerData(category1, type);
        int parent_id = ((DataResult)category1.getItem(0)).getInt("id");
        setSpinnerData(category2, parent_id);
        record_category_1.setAdapter(category1);
        record_category_2.setAdapter(category2);
        category_id = ((DataResult)category2.getItem(0)).getInt("id");
    }
    private void initListener(){
        record_toggle.setOnToggleChanged(toggleListener);
        record_category_1.setOnItemSelectedListener(itemSelectedListener);
        record_category_2.setOnItemSelectedListener(itemSelectedListener);
        record_save.setOnClickListener(clickListener);
    }


    private void setSpinnerData(SpinnerAdapter adapter, int parent_id){
        SpendTypeService spendTypeService = new SpendTypeService();
        List<DataResult> results = spendTypeService.getCatListByParentId(parent_id);
        adapter.setData(results);
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int parent_it = Integer.valueOf(view.getTag().toString());
            switch (parent.getId()){
                case R.id.record_category_1:
                    setSpinnerData(category2, parent_it);
                    category2.notifyDataSetChanged();
                    break;
                case R.id.record_category_2:
                    category_id = parent_it;
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FeeDao feeDao = new FeeDao();
            float fee = Float.valueOf(record_fee.getText().toString());
            if(fee != 0.0f){
                String desc = record_desc.getText().toString();
                int type = FeeDao.FEE_INCOME;
                if(record_toggle.isToggleOn()){
                    type = FeeDao.FEE_OUT;
                }
                fee = Utils.numLeft2(fee);
                long result = feeDao.insert(type, Dao.INT_NULL, Dao.INT_NULL, category_id, fee, UserDao.DEFAULT_USER_ID, desc);
                if(result > 0){
                    activity.finish();
                }
            }
        }
    };

    private ToggleButton.OnToggleChanged toggleListener = new ToggleButton.OnToggleChanged() {
        @Override
        public void onToggle(boolean on) {
            //true 表示支出
            String color = "#FF4081";
            if(on){
                color = "#33AE54";
                record_type.setText("支出");
                initSpinner(SpendTypeDao.TYPE_OUT);
            }
            else{
                record_type.setText("收入");
                initSpinner(SpendTypeDao.TYPE_IN);
            }
            record_fee.setTextColor(Color.parseColor(color));
            record_type.setTextColor(Color.parseColor(color));

//            category1.notifyDataSetChanged();
//            category2.notifyDataSetChanged();
        }
    };
}
