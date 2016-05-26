package com.whut.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whut.components.adapter.BlogAdapter;
import com.whut.components.adapter.IOnItemClicked;
import com.whut.view.ContainerActivity;
import com.whut.view.R;
import com.whut.view.fragment.style.SpaceItemDecoration;

import cn.edu.whut.lib.blog.dao.BlogDao;
import cn.edu.whut.lib.blog.service.BlogService;

public class BlogFragment extends Fragment{

    private AppCompatActivity activity;
    private FloatingActionButton blog_add;

    private BlogAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragment = LayoutInflater.from(container.getContext()).inflate(
                R.layout.blog_fragment, container, false
        );

        Toolbar toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        init(fragment);
        RecyclerView recyclerView = (RecyclerView) fragment.findViewById(R.id.blog_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        int space = getResources().getDimensionPixelSize(R.dimen.text_margin);
        recyclerView.addItemDecoration(new SpaceItemDecoration(space));
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
        initListerer();
    }
    private void initView(View parent){
        adapter = new BlogAdapter();
        blog_add = (FloatingActionButton) parent.findViewById(R.id.blog_add);
    }
    private void initData(){
        BlogService blogService = new BlogService();
        adapter.setData(blogService.getBlogList());
    }
    private void initListerer(){
        adapter.setItemListener(itemClicked);
        blog_add.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.blog_add:
                    Intent intent = new Intent(activity, ContainerActivity.class);
                    intent.putExtra(ContainerActivity.FRAGMENT, ContainerActivity.BLOG_EDIT);
                    startActivity(intent);
                    break;
            }
        }
    };

    private IOnItemClicked itemClicked = new IOnItemClicked() {
        @Override
        public void onClick(RecyclerView.ViewHolder holder, int position, Object tag) {
            if(tag != null && tag instanceof Integer){
                int blog_id = Integer.valueOf(tag.toString());
                Intent intent = new Intent(activity, ContainerActivity.class);
                intent.putExtra(ContainerActivity.FRAGMENT, ContainerActivity.BLOG_EDIT);
                Bundle params = new Bundle();
                params.putInt("blog_id", blog_id);
                intent.putExtra(ContainerActivity.PARAMS, params);
                startActivity(intent);
            }
        }
    };


}
