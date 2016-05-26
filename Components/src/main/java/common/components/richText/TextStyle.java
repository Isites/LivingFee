package common.components.richText;

import android.graphics.Typeface;

import common.components.richText.style.ImageStyle;
import common.components.richText.style.LiStyle;
import common.components.richText.style.QuoteStyle;
import common.components.richText.style.SimpleStyle;

public enum TextStyle {
    /**
     * 第一个表示类型
     * 第二个表示关联的类
     */
    BOLD(0x01, SimpleStyle.class),
    QUOTE(0x02, QuoteStyle.class),
    LI(0x03, LiStyle.class),
    IMAGE(0x04, ImageStyle.class);
    private int style;
    private Class clazz;

    /**
     * @param style
     */
    private TextStyle(int style, Class clazz) {
        this.style = style;
        this.clazz = clazz;
    }
    public int getStyle(){
        return this.style;
    }
    public Class getStyleClass(){
        return this.clazz;
    }

    /**
     *
     * @param style
     * @return
     */
    public static boolean isSurpport(int style){
        for (TextStyle myStyle : TextStyle.values()){
            if(myStyle.getStyle() == style){
                return true;
            }
        }
        return false;
    }
    public static TextStyle getStyleByClass(Class clazz){
        for (TextStyle myStyle : TextStyle.values()){
            if(myStyle.getStyleClass().getName().equals(clazz.getName())){
                return myStyle;
            }
        }
        return null;
    }
}
