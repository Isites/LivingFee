package com.whut.view;


import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.whut.components.activity.CommonActivity;
import com.whut.view.fragment.BlogFragment;
import com.whut.view.fragment.BudgetFragment;
import com.whut.view.fragment.FeesFragment;


public class MainActivity extends CommonActivity {

    public static final int TOOL_FEES = 0;
    public static final int TOOL_BUDGET = 1;
    public static final int TOOL_BLOG = 2;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private ImageButton fees;
    private ImageButton budget;
    private ImageButton blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);

        init();
    }
    private void init(){
        fees = (ImageButton) findViewById(R.id.tool_fees);
        budget = (ImageButton) findViewById(R.id.tool_budget);
        blog = (ImageButton) findViewById(R.id.tool_blog);
        fees.setOnClickListener(listener);
        budget.setOnClickListener(listener);
        blog.setOnClickListener(listener);
    }

    //在这里自动刷新界面
    @Override
    protected void onStart() {
        super.onStart();

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tool_fees:
                    mViewPager.setCurrentItem(TOOL_FEES, true);
                    break;
                case R.id.tool_budget:
                    mViewPager.setCurrentItem(TOOL_BUDGET, true);
                    break;
                case R.id.tool_blog:
                    mViewPager.setCurrentItem(TOOL_BLOG, true);
                    break;
            }
        }
    };

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new FeesFragment();
            switch (position){
                case TOOL_FEES:
                    fragment = new FeesFragment();
                    break;
                case TOOL_BUDGET:
                    fragment = new BudgetFragment();
                    break;
                case TOOL_BLOG:
                    fragment = new BlogFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
