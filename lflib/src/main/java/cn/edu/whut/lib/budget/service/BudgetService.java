package cn.edu.whut.lib.budget.service;

import cn.edu.whut.lib.budget.dao.BudgetDao;
import cn.edu.whut.lib.common.util.DateUtils;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.sqlite.DBHelper;
import cn.edu.whut.lib.user.dao.UserDao;

public class BudgetService {

    private static BudgetDao dao = new BudgetDao();



    //设置预算
    public long setMonthBudget(float fee, int spend_id){
        dao.setDao(DBHelper.openWrite());
        String from = Utils.date2String(DateUtils.getFirstDayOfMonth());
        String to = Utils.date2String(DateUtils.getLastDayOfMonth());

        //判断是否本月已经设置过预算
        if(dao.getBudgetsByTime(UserDao.DEFAULT_USER_ID,spend_id, from, to).size() > 0){
            return dao.update(fee, spend_id);
        }
        else{
            return dao.insert(spend_id, fee, UserDao.DEFAULT_USER_ID);
        }

//        dao.insert();
//        dao.update()
    }




}
