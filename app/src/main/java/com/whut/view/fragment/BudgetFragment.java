package com.whut.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.rebound.ui.Util;
import com.whut.components.adapter.BudgetAdapter;
import com.whut.components.adapter.IOnItemClicked;
import com.whut.view.ContainerActivity;
import com.whut.view.R;

import java.util.List;

import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.spendType.dao.SpendTypeDao;
import cn.edu.whut.lib.spendType.service.SpendTypeService;

public class BudgetFragment extends Fragment {

    private AppCompatActivity activity;

    private BudgetAdapter adapter;
    private TextView budget_total, budget_haveUsed, budget_totalLeft;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.budget_fragment, container, false);

        Toolbar toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        init(fragment);
        RecyclerView recyclerView = (RecyclerView) fragment.findViewById(R.id.budget_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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

    private void init(View parent){
        initView(parent);
        initData();
        initListener();
    }
    private void initView(View parent){
        adapter = new BudgetAdapter();

        budget_haveUsed = (TextView) parent.findViewById(R.id.budget_haveused);
        budget_totalLeft = (TextView) parent.findViewById(R.id.budget_totalLeft);
        budget_total = (TextView) parent.findViewById(R.id.budget_total);
    }
    private void initData(){
        SpendTypeService spendTypeService = new SpendTypeService();
        List<DataResult> datas = spendTypeService.getParentLists();
        adapter.setData(datas);
        float totalBudget = getTotalFees(datas, "fee");
        float totalOut = getTotalFees(datas, "totalOut");
        budget_total.setText(Utils.numLeft2(totalBudget) + "");
        budget_haveUsed.setText(Utils.numLeft2(totalOut) + "");
        budget_totalLeft.setText(Utils.numLeft2(totalBudget - totalOut) + "");

    }
    private void initListener(){
        adapter.setItemListener(itemClicked);
    }


    private float getTotalFees(List<DataResult> datas, String prop){
        float fees = 0.0f;
        for(DataResult data : datas){
            fees += data.getFloat(prop, 0f);
        }
        return fees;
    }
    private IOnItemClicked itemClicked = new IOnItemClicked() {
        @Override
        public void onClick(RecyclerView.ViewHolder holder, int position, Object tag) {
            if(tag != null && tag instanceof Integer){
                int parent_id = Integer.valueOf(tag.toString());
                Intent intent = new Intent(activity, ContainerActivity.class);
                intent.putExtra(ContainerActivity.FRAGMENT, ContainerActivity.BUDGET_2);
                Bundle params = new Bundle();
                params.putInt("parent_id", parent_id);
                intent.putExtra(ContainerActivity.PARAMS, params);
                startActivity(intent);
            }
        }
    };


}
