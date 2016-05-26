package cn.edu.whut.lib.budget.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;
import java.util.List;

import cn.edu.whut.lib.common.ContentPut;
import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.DateUtils;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.fee.dao.FeeDao;

public class BudgetDao extends FeeDao{

    public long insert(int spend_id, float fee, int user_id){
        return insert(FEE_BUDGET, INT_NULL, INT_NULL, spend_id, fee, user_id, null);
    }

    public int update(final float fee, int spend_id){
        //只修改本月的预算
        String from = Utils.date2String(DateUtils.getFirstDayOfMonth());
        String to = Utils.date2String(DateUtils.getLastDayOfMonth());

        String where = " spend_id = " + spend_id;
        where += " and update_time > '" + from + "' and update_time < '" + to + "' ";
        where += " and type = " + FEE_BUDGET;
        return update(DB_NAME, new ContentPut() {
            @Override
            public void put(ContentValues values) {
                Date now = new Date();
                values.put("update_time", Utils.date2String(now));
                values.put("fee", fee);
            }
        }, where, null);
    }

    public List<DataResult> getBudgetsByTime(int user_id, int spend_id, String from , String to){
        String sql = "select * from in_out_fee where type = " + FEE_BUDGET;
        sql += " and user_id = " + user_id;
        sql += " and spend_id = " + spend_id;
        sql += " and update_time > '" + from + "' and update_time < '" + to +"' ";
        Cursor cursor = dao.rawQuery(sql, null);
        return rows2Data(cursor);
    }

//    public DataResult getOneBudget(int spend_id, int user_id){
//        String sql = "select spend_type.*, spend_type.fee, ";
//
//    }
}
