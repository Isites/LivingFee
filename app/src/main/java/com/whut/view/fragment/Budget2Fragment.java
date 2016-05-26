package com.whut.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.whut.components.adapter.BudgetAdapter;
import com.whut.components.adapter.IOnItemClicked;
import com.whut.view.R;

import cn.edu.whut.lib.budget.service.BudgetService;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.spendType.dao.SpendTypeDao;
import cn.edu.whut.lib.spendType.service.SpendTypeService;

public class Budget2Fragment extends Fragment{


    private AppCompatActivity activity;
    private IOnItemClicked itemClicked;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.budget2_fragment, container, false);

        Toolbar toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        init();

        RecyclerView recyclerView = (RecyclerView) fragment.findViewById(R.id.budget2_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        SpendTypeService spendTypeService = new SpendTypeService();
        BudgetAdapter adapter = new BudgetAdapter();
        Bundle bundle = getArguments();
        adapter.setData(spendTypeService.getCatListByParentId(bundle.getInt("parent_id", SpendTypeDao.TYPE_OUT)));
        adapter.setItemListener(itemClicked);
        recyclerView.setAdapter(adapter);
        return fragment;
    }
    private void init(){
        itemClicked = new IOnItemClicked() {
            @Override
            public void onClick(RecyclerView.ViewHolder holder, int position, final Object tag) {
                final BudgetAdapter.ViewHolder viewHolder = (BudgetAdapter.ViewHolder) holder;
                View dialog_edit = LayoutInflater.from(activity)
                        .inflate(R.layout.dialog_edit, null);

                final EditText editText = (EditText) dialog_edit.findViewById(R.id.dialog_budget_edit);
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setTitle("设置预算")
                        .setView(dialog_edit)
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                float fee = Utils.numLeft2(Float.valueOf(editText.getText().toString()));
                                if(tag != null && tag instanceof Integer){
                                    int spend_id = Integer.valueOf(tag.toString());
                                    BudgetService budgetService = new BudgetService();
                                    budgetService.setMonthBudget(fee, spend_id);
                                    viewHolder.budget_fee.setText(String.valueOf(fee));
                                }
                            }
                        }).show();
            }
        };
    }
}
