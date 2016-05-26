package cn.edu.whut.lib.spendType.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

import cn.edu.whut.lib.common.ContentPut;
import cn.edu.whut.lib.common.Dao;
import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.fee.dao.FeeDao;

public class SpendTypeDao extends Dao{

    public static final String DB_NAME = "spend_type";
    //目前分为3级分类

    public static final int TYPE_ALL = 0;

    public static final int TYPE_IN = -1; //收入分类
    public static final int TYPE_OUT = -2; //支出分类

    public long insert(final String resource, final String name, final int parent_id){
        return insert(DB_NAME, new ContentPut() {
            @Override
            public void put(ContentValues values) {
                values.put("resource", resource);
                values.put("name", name);
                values.put("parent_id", parent_id);
            }
        });
    }

    public DataResult getCatByName(String name){
        String sql = "select * from spend_type ";
        sql += " where name = '" + name + "' limit 1";
        Cursor cursor = dao.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            return row2Data(cursor);
        }
        return new DataResult();
    }

    public List<DataResult> getCatList(int parent_id){
        String sql = "select spend_type.*, fee from spend_type left join in_out_fee " ;
        sql += " on in_out_fee.spend_id = spend_type.id and in_out_fee.type = " + FeeDao.FEE_BUDGET;
        sql += " where parent_id = "+ parent_id;
        Cursor cursor = dao.rawQuery(sql, null);
        return rows2Data(cursor);
    }


}
