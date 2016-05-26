package com.whut.view.fragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.whut.components.adapter.FeesAdapter;
import com.whut.components.adapter.IOnItemClicked;
import com.whut.view.ContainerActivity;
import com.whut.view.R;

import java.util.Calendar;
import java.util.Date;

import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.fee.dao.FeeDao;
import cn.edu.whut.lib.fee.service.FeesService;
import cn.edu.whut.lib.user.dao.UserDao;

public class FeesFragment extends Fragment {

    private AppCompatActivity activity;

    private Button record;
    private TextView fees_income;
    private TextView fees_out;

    private FeesAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fees_fragment, container, false);

        Toolbar toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        init(fragment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = (RecyclerView) fragment.findViewById(R.id.fees_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        adapter.notifyDataSetChanged();
    }

    private void init(View view){
        initView(view);
        initData();
        initListener();
    }
    private void initView(View rootView){
        adapter = new FeesAdapter();
        record = (Button) rootView.findViewById(R.id.fees_record);
        fees_income = (TextView) rootView.findViewById(R.id.fees_income);
        fees_out = (TextView) rootView.findViewById(R.id.fees_out);
    }
    private void initData(){
        FeesService feesService = new FeesService();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH)+1;
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date monthFrom = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthTo = cal.getTime();
        DataResult result = null;
        result = feesService.getFeesOfList(UserDao.DEFAULT_USER_ID, FeeDao.FEE_INCOME,
                Utils.date2String(monthFrom), Utils.date2String(monthTo));
        fees_income.setText("￥ " + Utils.numLeft2(result.getFloat("totalFee")));
        result = feesService.getFeesOfList(UserDao.DEFAULT_USER_ID, FeeDao.FEE_OUT,
                Utils.date2String(monthFrom), Utils.date2String(monthTo));
        fees_out.setText("￥ " + Utils.numLeft2(result.getFloat("totalFee")));
    }
    private void initListener(){
        record.setOnClickListener(listener);
        adapter.setItemListener(itemClicked);
    }


    private IOnItemClicked itemClicked = new IOnItemClicked() {
        @Override
        public void onClick(RecyclerView.ViewHolder holder, int position, Object tag) {
            Intent intent = new Intent(activity, ContainerActivity.class);

            Bundle params = new Bundle();
            params.putInt("section", Integer.valueOf(tag.toString()));
            intent.putExtra(ContainerActivity.PARAMS, params);
            intent.putExtra(ContainerActivity.FRAGMENT, ContainerActivity.FEES_QUERY);
            startActivity(intent);
        }
    };

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fees_record:
                    Intent intent = new Intent(activity, ContainerActivity.class);
                    intent.putExtra(ContainerActivity.FRAGMENT, ContainerActivity.RECORD);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                    }
                    else{
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

}
