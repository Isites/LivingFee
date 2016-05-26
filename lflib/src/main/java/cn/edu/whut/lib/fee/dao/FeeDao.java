package cn.edu.whut.lib.fee.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;
import java.util.List;

import cn.edu.whut.lib.common.ContentPut;
import cn.edu.whut.lib.common.Dao;
import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.Utils;

public class FeeDao extends Dao{
    public static final String DB_NAME = "in_out_fee";
    public static final int FEE_INCOME = 1;//收入
    public static final int FEE_OUT = 2; //支出
    public static final int FEE_BUDGET = 3; //月预算
//    public static final int WANTED = 4; //想要的

    public long insert(final int type, final int blog_id, final int account_id,
                final int spend_id, final float fee, final int user_id, final String description ){
        return insert(DB_NAME, new ContentPut() {
            @Override
            public void put(ContentValues values) {
                values.put("type", type);
                values.put("fee", fee);
                values.put("user_id", user_id);
                if(blog_id != INT_NULL){
                    values.put("blog_id", blog_id);
                }
                if(account_id != INT_NULL){
                    values.put("account_id", account_id);
                }
                if(spend_id != INT_NULL){
                    values.put("spend_id", spend_id);
                }
                if(description != null){
                    values.put("description", description);
                }
                Date date = new Date();
                String now = Utils.date2String(date);
                values.put("create_time", now);
                values.put("update_time", now);
            }
        });
    }
    //需要连接查询
    public DataResult getById(int id){
        String sql = "select * from in_out_fee where id = "+ id;
        Cursor cursor = dao.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            return row2Data(cursor);
        }
        return new DataResult();
    }
    public List<DataResult> getByUserId(int user_id,int type,int pageNum, int page, String from, String to){
        page -= 1;
        if(page < 0) page = 0;
        //偏移量
        page = pageNum * page;
        String sql = "select in_out_fee.*, spend_type.name, spend_type.resource from in_out_fee left join spend_type ";
        sql += " on spend_type.id = spend_id where update_time > '" + from + "' ";
        sql += " and update_time < '" + to +"' and user_id = " + user_id ;
        sql += " and type = '"+ type +"' ";
        sql += " limit " + pageNum + " offset " + page;
        Cursor cursor = dao.rawQuery(sql, null);
        return rows2Data(cursor);
    }
    public List<DataResult> getByTypes(int user_id, int pageNum, int page, String from, String to, int...types){
        page -= 1;
        if(page < 0) page = 0;
        //偏移量
        page = pageNum * page;
        String where_type = "(";
        if(types.length == 0){
            where_type = "("+FEE_INCOME+","+FEE_OUT+")";
        }
        else{
            for(int type : types){
                where_type += type + ",";
            }
            where_type = where_type.substring(where_type.length() - 1) + ")";
        }
        String sql = "select in_out_fee.*, spend_type.name, spend_type.resource from in_out_fee left join spend_type ";
        sql += " on spend_type.id = spend_id where update_time > '" + from + "' ";
        sql += " and update_time < '" + to +"' and user_id = " + user_id ;
        sql += " and type in "+ where_type +"  ";
        sql += " limit " + pageNum + " offset " + page;
        Cursor cursor = dao.rawQuery(sql, null);
        List<DataResult> results = rows2Data(cursor);
        return results;
    }

    public List<DataResult> getAllByUserId(int user_id, int type, String from, String to){
        //只取一页，读取所有数据
        return getByUserId(user_id, type, Integer.MAX_VALUE, 1, from, to);
    }

    public List<DataResult> getListsBySpendType(int type,int user_id,  int spend_id, String from, String to){
        String sql = "select * from in_out_fee ";
        sql += " where type = " + type + " and spend_id = " + spend_id;
        sql += " and user_id = " + user_id;
        sql += " and update_time > '" + from + "' " + " and update_time < '" + to +"' ";
        Cursor cursor = dao.rawQuery(sql, null);
        return rows2Data(cursor);
    }

}
