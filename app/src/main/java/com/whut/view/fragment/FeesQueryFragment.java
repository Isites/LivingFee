package com.whut.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.whut.components.adapter.FeeQueryAdapter;
import com.whut.components.adapter.SpinnerAdapter;
import com.whut.view.R;

import java.util.Date;
import java.util.List;

import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.DateUtils;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.fee.service.FeesService;
import cn.edu.whut.lib.spendType.dao.SpendTypeDao;
import cn.edu.whut.lib.spendType.service.SpendTypeService;
import cn.edu.whut.lib.user.dao.UserDao;

public class FeesQueryFragment extends Fragment{


    public static final int SECTION_DAY = 0;
    public static final int SECTION_WEEK = 1;
    public static final int SECTION_MONTH = 2;

    private AppCompatActivity activity;

    private RecyclerView recyclerView;
    private AppCompatSpinner fees_query_category1, fees_query_category2;
    private TextView fees_query_income, fees_query_out;

    private SpinnerAdapter category1, category2;

    private int category_id;

    private FeeQueryAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fees_query_fragment, container, false);
        Toolbar toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        init(fragment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) fragment.findViewById(R.id.fees_query_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return fragment;
    }

    private void init(View parent){
        initView(parent);
        initData();
        initListener();
    }
    private void initView(View parent){
        adapter = new FeeQueryAdapter();

        fees_query_category1 = (AppCompatSpinner) parent.findViewById(R.id.fees_query_category1);
        fees_query_category2 = (AppCompatSpinner) parent.findViewById(R.id.fees_query_category2);

        fees_query_income = (TextView) parent.findViewById(R.id.fees_query_income);
        fees_query_out = (TextView) parent.findViewById(R.id.fees_query_out);
    }
    private void initData(){
        category1 = new SpinnerAdapter();
        category2 = new SpinnerAdapter();

        setListsData();
        SpendTypeService spendTypeService = new SpendTypeService();
        List<DataResult> category_lists = spendTypeService.getCatListByParentId(SpendTypeDao.TYPE_OUT);
        category_lists.addAll(spendTypeService.getCatListByParentId(SpendTypeDao.TYPE_IN));
        DataResult result_all = new DataResult();
        result_all.setAttr("name", "全部");
        result_all.setAttr("id", SpendTypeDao.TYPE_ALL);
        category_lists.add(0, result_all);
        category1.setData(category_lists);
        setSpinnerData(category2, category_lists.get(1).getInt("id"));

        fees_query_category1.setAdapter(category1);
        fees_query_category2.setAdapter(category2);

    }
    private void initListener(){
        fees_query_category1.setOnItemSelectedListener(itemSelectedListener);
        fees_query_category2.setOnItemSelectedListener(itemSelectedListener);
    }


    private void setListsData(int...spend_types){
        int section = 0;
        Bundle bundle = getArguments();
        if(bundle != null){
            section = bundle.getInt("section");
        }
        String[] dates = getDateSection(section);

        FeesService feesService = new FeesService();
        DataResult result = feesService.getAllLists(UserDao.DEFAULT_USER_ID, dates[0], dates[1], spend_types);
        adapter.setData((List<DataResult>) result.getObj("lists"));
        fees_query_income.setText(result.getFloat("totalIncome") + "");
        fees_query_out.setText(result.getFloat("totalOut") + "");
    }
    private String[] getDateSection(int section){
        String[] date2 = new String[2];
        Date now = new Date();
        String from = Utils.date2ShortStr(now) + " 00:00:00";
        String to = Utils.date2ShortStr(now) + " 23:59:59";
        switch(section){
            case SECTION_DAY:
                break;
            case SECTION_WEEK:
                from = Utils.date2ShortStr(DateUtils.getFirstDayOfWeek()) + " 00:00:00";
                to = Utils.date2ShortStr(DateUtils.getLastDayOfWeek()) + " 23:59:59";
                break;
            case SECTION_MONTH:
                from = Utils.date2ShortStr(DateUtils.getFirstDayOfMonth()) + " 00:00:00";
                to = Utils.date2ShortStr(DateUtils.getLastDayOfMonth()) + " 23:59:59";
                break;
        }
        date2[0] = from;
        date2[1] = to;
        return date2;
    }
    private void setSpinnerData(SpinnerAdapter adapter, int parent_id){
        if(parent_id == SpendTypeDao.TYPE_ALL) return;
        SpendTypeService spendTypeService = new SpendTypeService();
        List<DataResult> results = spendTypeService.getCatListByParentId(parent_id);
        DataResult result_all = new DataResult();
        result_all.setAttr("name", "全部");
        result_all.setAttr("id", SpendTypeDao.TYPE_ALL);
        results.add(0, result_all);
        adapter.setData(results);
    }
    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int parent_id = Integer.valueOf(view.getTag().toString());
            switch (parent.getId()){
                case R.id.fees_query_category1:
                    if(parent_id == SpendTypeDao.TYPE_ALL){
                        setListsData();
                    }
                    else{
                        DataResult result = (DataResult) fees_query_category2.getSelectedItem();
                        setSpinnerData(category2, parent_id);
                        if(result.getInt("id") == SpendTypeDao.TYPE_ALL){
                            List<DataResult> datas = new SpendTypeService().getCatListByParentId(parent_id);
                            int[] spend_types = new int[datas.size()];
                            for(int i = 0; i < spend_types.length; i++){
                                spend_types[i] = datas.get(i).getInt("id");
                            }
                            setListsData(spend_types);
                        }
                        else{
                            setListsData(result.getInt("id"));
                        }
                    }
                    break;
                case R.id.fees_query_category2:
                    if(parent_id == SpendTypeDao.TYPE_ALL){
                        DataResult result = (DataResult) fees_query_category1.getSelectedItem();
                        parent_id = result.getInt("id");
                        List<DataResult> datas = new SpendTypeService().getCatListByParentId(parent_id);
                        int[] spend_types = new int[datas.size()];
                        for(int i = 0; i < spend_types.length; i++){
                            spend_types[i] = datas.get(i).getInt("id");
                        }
                        setListsData(spend_types);
                    }
                    else{
                        setListsData(parent_id);
                    }
                    break;
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

}
