package common.components.richText;

import java.lang.reflect.Constructor;

public class TextUtil {

    public static <T> T getInstance(TextEditor editor, TextStyle style)  {
        Object[] args = StyleConfig.getParams(style);
        Object[] params = new Object[args.length+1];
        params[0] = editor;
        for(int i = 1; i < params.length; i++){
            params[i] = args[i-1];
        }
        args = null;
        try {
            return getInstance(style, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T getInstance(TextStyle style, Object...args) throws Exception {

        Class<T> clazz = style.getStyleClass();
        Class[] argclazz = new Class[args.length];
        for(int i = 0; i < args.length; i++){
            argclazz[i] = args[i].getClass();
        }
        Constructor<T> constructor = null;
        try {
             constructor = clazz.getConstructor(argclazz);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return constructor == null ? null : constructor.newInstance(args);
    }

}
