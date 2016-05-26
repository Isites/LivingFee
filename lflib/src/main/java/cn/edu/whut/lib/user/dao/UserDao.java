package cn.edu.whut.lib.user.dao;

import android.content.ContentValues;

import cn.edu.whut.lib.common.ContentPut;
import cn.edu.whut.lib.common.Dao;

public class UserDao extends Dao{

    public static final  String DB_NAME = "user";
    public static final int DEFAULT_USER_ID = 1;



    public long insert(final String user_name, final String user_pass){
        return insert(DB_NAME, new ContentPut() {
            @Override
            public void put(ContentValues values) {
                values.put("user_name", user_name);
                values.put("user_pass", user_pass);
            }
        });
    }
}
