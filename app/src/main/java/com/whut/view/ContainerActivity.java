package com.whut.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;

import com.whut.components.activity.CommonActivity;
import com.whut.view.fragment.BlogEditFragment;
import com.whut.view.fragment.Budget2Fragment;
import com.whut.view.fragment.FeesQueryFragment;
import com.whut.view.fragment.RecordFragment;
import com.whut.view.fragment.listener.IOnKeyListener;

public class ContainerActivity extends CommonActivity {


    private IOnKeyListener keyListener;

    public static final String FRAGMENT = "Fragment";
    public static final String PARAMS = "params";

    public static final int RECORD = 1;
    public static final int BUDGET_2 = 2;
    public static final int BLOG_EDIT = 3;
    public static final int FEES_QUERY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyListener != null){
            keyListener.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init(){
        Intent intent = getIntent();
        int tag = intent.getIntExtra(FRAGMENT, RECORD);
        Bundle bundle = intent.getBundleExtra(PARAMS);
        Fragment fragment = getFragment(tag);
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.second_container,fragment).commit();
    }


    private Fragment getFragment(int tag){
        Fragment fragment = null;
        switch (tag){
            case RECORD:
                fragment = new RecordFragment();
                break;
            case BUDGET_2:
                fragment = new Budget2Fragment();
                break;
            case BLOG_EDIT:
                fragment = new BlogEditFragment();
                break;
            case FEES_QUERY:
                fragment = new FeesQueryFragment();
        }
        return fragment;
    }

    public void setKeyListener(IOnKeyListener keyListener) {
        this.keyListener = keyListener;
    }

}
