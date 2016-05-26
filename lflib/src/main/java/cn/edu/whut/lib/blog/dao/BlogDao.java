package cn.edu.whut.lib.blog.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;
import java.util.List;

import cn.edu.whut.lib.common.ContentPut;
import cn.edu.whut.lib.common.Dao;
import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.Utils;

public class BlogDao extends Dao{

    public static final String DB_NAME = "blog";

    public long insert(final String content, final int fee_id){
        return insert(DB_NAME, new ContentPut() {
            @Override
            public void put(ContentValues values) {
                String now = Utils.date2String(new Date());
                if(fee_id != INT_NULL){
                    values.put("fee_id", fee_id);
                }
                values.put("content", content);
                values.put("create_time", now);
                values.put("update_time", now);
            }
        });
    }
    public int update(int blog_id, final String content){
        String where = " id = " + blog_id;
        return update(DB_NAME, new ContentPut() {
            @Override
            public void put(ContentValues values) {
                String now = Utils.date2String(new Date());
                values.put("content", content);
                values.put("update_time", now);
            }
        }, where, null);
    }

    public DataResult getBlogById(int id){
        String sql = "select * from blog where id = ";
        sql += id;
        Cursor cursor = dao.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            return row2Data(cursor);
        }
        return new DataResult();
    }
    public List<DataResult> getBlogList(int page, int pageNum){
        page -= 1;
        if(page < 0) page = 0;
        //偏移量
        page = pageNum * page;
        String sql = "select * from blog ";
        sql += " limit " + pageNum + " offset " + page;
        Cursor cursor = dao.rawQuery(sql, null);
        return rows2Data(cursor);
    }
}
