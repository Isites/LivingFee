package cn.edu.whut.lib.blog.service;

import java.util.List;

import cn.edu.whut.lib.blog.dao.BlogDao;
import cn.edu.whut.lib.common.DataResult;

public class BlogService {
    private static BlogDao dao = new BlogDao();

    public List<DataResult> getBlogList(){
        return dao.getBlogList(1, Integer.MAX_VALUE);
    }

    public long insert(String content, int fee_id){
        return dao.insert(content, fee_id);
    }

    public int update(int blog_id, String content){
        return dao.update(blog_id, content);
    }

}
