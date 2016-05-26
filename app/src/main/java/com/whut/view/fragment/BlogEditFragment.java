package com.whut.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whut.view.ContainerActivity;
import com.whut.view.R;
import com.whut.view.fragment.listener.IOnKeyListener;

import cn.edu.whut.lib.blog.dao.BlogDao;
import cn.edu.whut.lib.blog.service.BlogService;
import cn.edu.whut.lib.common.Dao;
import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.spendType.dao.SpendTypeDao;
import common.components.richText.TextEditor;

public class BlogEditFragment extends Fragment{

    private TextEditor editor;

    private ContainerActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ContainerActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = LayoutInflater.from(container.getContext()).inflate(
                R.layout.blog_edit_fragment, container, false
        );

        Toolbar toolbar = (Toolbar) fragment.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        init(fragment);

        return fragment;
    }

    private void init(View parent){
        initView(parent);
        initData();
        initListener();
    }
    private void initView(View parent){
        editor = (TextEditor) parent.findViewById(R.id.blog_editor);
        editor.post(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                if(bundle != null){
                    int blog_id = bundle.getInt("blog_id", Dao.INT_NULL);
                    if(blog_id != Dao.INT_NULL){
                        BlogDao blogDao = new BlogDao();
                        DataResult result = blogDao.getBlogById(blog_id);
                        editor.showHtml(result.getString("content"));
                    }
                }
            }
        });
    }
    private void initData(){}
    private void initListener(){
        activity.setKeyListener(keyListener);
    }

    public String getCurrContent(){
        return editor.getText().toString();
    }
    private void save(){
        Bundle bundle = getArguments();
        BlogService blogService = new BlogService();
        String content = editor.getText().toString();
        if(bundle != null) {
            int blog_id = bundle.getInt("blog_id", Dao.INT_NULL);
            if(content != null && content.length() > 0){
                blogService.update(blog_id, content);
            }
        }
        else{
            if(content != null && content.length() > 0){
                blogService.insert(content, Dao.INT_NULL);
            }
        }
    }
    private IOnKeyListener keyListener = new IOnKeyListener() {
        @Override
        public void onKeyDown(int keyCode, KeyEvent event) {
            switch (keyCode){
                case  KeyEvent.KEYCODE_BACK:
                    save();
                    break;
            }
        }
    };


}
