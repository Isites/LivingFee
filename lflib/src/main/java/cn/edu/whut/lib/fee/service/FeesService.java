package cn.edu.whut.lib.fee.service;

import java.util.List;

import cn.edu.whut.lib.common.DataResult;
import cn.edu.whut.lib.fee.dao.FeeDao;
import cn.edu.whut.lib.sqlite.DBHelper;

public class FeesService {
    private static final FeeDao dao = new FeeDao();

    //得到所有的流水记录
    public List<DataResult> getFlowLists(int user_id, int type, String from, String to){
        dao.setDao(DBHelper.openRead());
        return dao.getAllByUserId(user_id, type, from, to);
    }
    //得到某一短时间的流水的统计
    public DataResult getFeesOfList(int user_id, int type, String from, String to){
        DataResult result = new DataResult();
        List<DataResult> lists = getFlowLists(user_id, type, from, to);
        float fees = 0.0f;
        for (DataResult data : lists){
            float fee = data.getFloat("fee");
            fees += fee;
        }
        result.setAttr("totalFee", fees);
        result.setAttr("lists", lists);
        return result;
    }

    private boolean isInTypes(int id, int...types){
        for(int spend : types){
            if(spend == id){
                return true;
            }
        }
        return false;
    }
    public DataResult getAllLists(int user_id, String from, String to, int...spend_types){
        dao.setDao(DBHelper.openRead());
        List<DataResult> lists = dao.getByTypes(user_id,Integer.MAX_VALUE, 1, from, to);
        if(spend_types.length > 0){
            for(int i = 0; i < lists.size();){
                if(!isInTypes(lists.get(i).getInt("spend_id"), spend_types)){
                    lists.remove(i);
                }
                else{
                    i++;
                }
            }
        }
        float income_fees = 0.0f;
        float out_fees = 0.0f;
        for (DataResult data : lists){
            if(data.getInt("type") == FeeDao.FEE_INCOME){
                income_fees += data.getFloat("fee");
            }
            else if(data.getInt("type") == FeeDao.FEE_OUT){
                out_fees += data.getFloat("fee");
            }
        }
        DataResult result = new DataResult();
        result.setAttr("totalIncome", income_fees);
        result.setAttr("totalOut", out_fees);
        result.setAttr("lists", lists);
        return result;
    }

    public long insert(int type, int blog_id, int account_id, int spend_id, float fee, int user_id, String description ){
        dao.setDao(DBHelper.openWrite());
        return dao.insert(type, blog_id, account_id, spend_id, fee, user_id, description);
    }

}
