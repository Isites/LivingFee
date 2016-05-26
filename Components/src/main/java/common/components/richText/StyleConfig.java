package common.components.richText;

import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

public class StyleConfig {
    // TODO: 2016/5/7 可以把这些东西配置到数据库里面
    private static class Param{
        public int style;
        public Object [] params;
        public Param(int style, Object...args){
            this.style = style;
            this.params = args;
        }
    }
    private static List<Param> params = new ArrayList<Param>();
    public static void init(){
        //这里int类型被强制转换成了对象 即integer
        params.add(new Param(TextStyle.BOLD.getStyle(), new Object[]{Typeface.BOLD}));
        params.add(new Param(TextStyle.LI.getStyle(), new Object[]{0, 0, 0}));
        params.add(new Param(TextStyle.QUOTE.getStyle(), new Object[]{0, 0, 0}));
    }
    //参数的顺序很重要
    public static void setAttrs(TextStyle style, int...attrs){
        for(Param param : params){
            if(param.style == style.getStyle()){
                if(param.params.length >= attrs.length){
                    for(int i = 0;i < attrs.length; i++){
                        param.params[i] = attrs[i];
                    }
                }
            }
        }
    }
    public static Object[] getParams(TextStyle style){
        for(Param param : params){
            if(param.style == style.getStyle()){
                return param.params;
            }
        }
        //返回空的list
        return new Object[0];
    }

}
