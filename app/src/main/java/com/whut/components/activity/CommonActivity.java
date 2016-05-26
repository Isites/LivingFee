package com.whut.components.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.whut.view.R;


import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.spendType.dao.SpendTypeDao;
import cn.edu.whut.lib.sqlite.DBHelper;
import cn.edu.whut.lib.user.dao.UserDao;


public class CommonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper.init(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initDBData();
            }
        }).start();
    }

    private void initDBData(){
        SharedPreferences sharedPreferences = getSharedPreferences("LivingFee", AppCompatActivity.MODE_PRIVATE);
        //只执行一次
        if(sharedPreferences.getString("DBDATA", null) == null){
            UserDao defaultUser = new UserDao();
            defaultUser.insert("admin", "admin");

            SpendTypeDao typeDao = new SpendTypeDao();
            //支出
            //服饰类
            typeDao.insert(String.valueOf(R.drawable.ic_cloth), "衣服饰品", SpendTypeDao.TYPE_OUT);
            DataResult result = typeDao.getCatByName("衣服饰品");
            int parent_id = result.getInt("id");
            typeDao.insert(String.valueOf(R.drawable.cloth_jeans), "衣服裤子", parent_id);
            typeDao.insert(String.valueOf(R.drawable.cloth_makeup), "化妆饰品", parent_id);
            typeDao.insert(String.valueOf(R.drawable.cloth_hat), "鞋帽包包", parent_id);
            //食物
            typeDao.insert(String.valueOf(R.drawable.ic_food), "食品酒水", SpendTypeDao.TYPE_OUT);
            result = typeDao.getCatByName("食品酒水");
            parent_id = result.getInt("id");
            typeDao.insert(String.valueOf(R.drawable.food_lunch), "早午晚餐", parent_id);
            typeDao.insert(String.valueOf(R.drawable.food_junk), "水果零食", parent_id);
            typeDao.insert(String.valueOf(R.drawable.food_drink), "烟酒茶", parent_id);

            //交通出行
            typeDao.insert(String.valueOf(R.drawable.ic_car), "交通出行", SpendTypeDao.TYPE_OUT);
            result = typeDao.getCatByName("交通出行");
            parent_id = result.getInt("id");
            typeDao.insert(String.valueOf(R.drawable.car_bus), "公交", parent_id);
            typeDao.insert(String.valueOf(R.drawable.car_cz), "出租", parent_id);
            typeDao.insert(String.valueOf(R.drawable.car_ride), "自行车", parent_id);
            //学习进修
            typeDao.insert(String.valueOf(R.drawable.ic_study), "学习进修", SpendTypeDao.TYPE_OUT);
            result = typeDao.getCatByName("学习进修");
            parent_id = result.getInt("id");
            typeDao.insert(String.valueOf(R.drawable.study_fx), "辅修", parent_id);
            typeDao.insert(String.valueOf(R.drawable.study_px), "培训", parent_id);
            typeDao.insert(String.valueOf(R.drawable.study_sm), "数码设备", parent_id);
            typeDao.insert(String.valueOf(R.drawable.study_book), "书报杂志", parent_id);
            //其他费用
            typeDao.insert(String.valueOf(R.drawable.ic_common), "其他费用", SpendTypeDao.TYPE_OUT);
            result = typeDao.getCatByName("其他费用");
            parent_id = result.getInt("id");
            typeDao.insert(String.valueOf(R.drawable.common_fee), "公共费用", parent_id);
            typeDao.insert(String.valueOf(R.drawable.common_hq), "还人钱财", parent_id);
            typeDao.insert(String.valueOf(R.drawable.common_jr), "金融保险", parent_id);
            typeDao.insert(String.valueOf(R.drawable.common_yl), "医疗费用", parent_id);
            typeDao.insert(String.valueOf(R.drawable.common_rc), "日常用品", parent_id);
            typeDao.insert(String.valueOf(R.drawable.common_gift), "送礼请客", parent_id);
            typeDao.insert(String.valueOf(R.drawable.common_jz), "捐赠", parent_id);

            //交流通讯
            typeDao.insert(String.valueOf(R.drawable.ic_net), "交流通讯", SpendTypeDao.TYPE_OUT);
            result = typeDao.getCatByName("交流通讯");
            parent_id = result.getInt("id");
            typeDao.insert(String.valueOf(R.drawable.net_dl), "上网费", parent_id);
            typeDao.insert(String.valueOf(R.drawable.net_yj), "邮寄费", parent_id);
            typeDao.insert(String.valueOf(R.drawable.net_phone), "手机费", parent_id);
            //休闲娱乐
            typeDao.insert(String.valueOf(R.drawable.ic_fun), "休闲娱乐", SpendTypeDao.TYPE_OUT);
            result = typeDao.getCatByName("休闲娱乐");
            parent_id = result.getInt("id");
            typeDao.insert(String.valueOf(R.drawable.fun_yl), "休闲玩乐", parent_id);
            typeDao.insert(String.valueOf(R.drawable.fun_ly), "旅游", parent_id);
            typeDao.insert(String.valueOf(R.drawable.fun_jh), "聚会", parent_id);
            typeDao.insert(String.valueOf(R.drawable.fun_js), "健身", parent_id);
            typeDao.insert(String.valueOf(R.drawable.fun_cw), "宠物", parent_id);
            //收入
            typeDao.insert(String.valueOf(R.drawable.ic_income), "额外收入", SpendTypeDao.TYPE_IN);
            result = typeDao.getCatByName("额外收入");
            parent_id = result.getInt("id");
            typeDao.insert(String.valueOf(R.drawable.part_job), "兼职", parent_id);
            typeDao.insert(String.valueOf(R.drawable.student_price), "奖学金", parent_id);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("DBDATA", "haveInit");
            editor.commit();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
