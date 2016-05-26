package cn.edu.whut.lib.spendType.service;

import java.util.Date;
import java.util.List;

import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.common.util.DateUtils;
import cn.edu.whut.lib.common.util.Utils;
import cn.edu.whut.lib.fee.dao.FeeDao;
import cn.edu.whut.lib.fee.service.FeesService;
import cn.edu.whut.lib.spendType.dao.SpendTypeDao;
import cn.edu.whut.lib.sqlite.DBHelper;
import cn.edu.whut.lib.user.dao.UserDao;

public class SpendTypeService {
    private static final SpendTypeDao dao = new SpendTypeDao();


    //获取的二级分类列表
    public List<DataResult> getCatListByParentId(int parent_id){
        dao.setDao(DBHelper.openRead());
        List<DataResult> dataResults = dao.getCatList(parent_id);
        FeesService feesService = new FeesService();
        Date from = DateUtils.getFirstDayOfMonth();
        Date to= DateUtils.getLastDayOfMonth();
        for(DataResult data : dataResults){
            DataResult result = feesService.getAllLists(UserDao.DEFAULT_USER_ID, Utils.date2String(from), Utils.date2String(to), data.getInt("id"));
            data.setAttr("totalOut", Utils.numLeft2(result.getFloat("totalOut")));
        }
        return dataResults;
    }

    //获取一级分类列表及预算汇总

    public List<DataResult> getParentLists(){
        FeeDao feeDao = new FeeDao();
        FeesService feesService = new FeesService();
        Date from = DateUtils.getFirstDayOfMonth();
        Date to= DateUtils.getLastDayOfMonth();
        List<DataResult> parents = getCatListByParentId(SpendTypeDao.TYPE_OUT);
        for(DataResult parent : parents){
            float fee = 0.0f;
            List<DataResult> childs = getCatListByParentId(parent.getInt("id"));
            for(DataResult result : childs){
                List<DataResult> budgets = feeDao.getListsBySpendType(FeeDao.FEE_BUDGET, UserDao.DEFAULT_USER_ID,result.getInt("id"), Utils.date2String(from),Utils.date2String(to));
                for(DataResult budget : budgets){
                    fee += budget.getFloat("fee");
                }
            }
            int[] spend_types = new int[childs.size()];
            for(int i = 0; i < spend_types.length; i++){
                spend_types[i] = childs.get(i).getInt("id");
            }
            DataResult dataResult = feesService.getAllLists(UserDao.DEFAULT_USER_ID, Utils.date2String(from),Utils.date2String(to), spend_types);
            parent.setAttr("fee", Utils.numLeft2(fee));
            parent.setAttr("totalOut", dataResult.getFloat("totalOut"));
        }
        return parents;
    }



}
