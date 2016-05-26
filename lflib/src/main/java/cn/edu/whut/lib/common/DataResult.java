package cn.edu.whut.lib.common;

import android.util.Log;

import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;

public class DataResult {
    private Map map;
    public DataResult(){
        map = new HashMap();
    }
    public String getString(String key){
        Object obj = map.get(key);
        return String.valueOf(obj);
    }
    public float getFloat(String key){
        String data = getString(key);
        return Float.valueOf(data);
    }
    public float getFloat(String key, float defaultValue){
        String data = getString(key);
        if(data == null || data.equals("null")) return defaultValue;
        return Float.valueOf(data);
    }
    public int getInt(String key){
        String data = getString(key);
        return Integer.valueOf(data);
    }
    public int getInt(String key, int defaultValue){
        String data = getString(key);
        if(data == null || data.equals("null")) return defaultValue;
        return Integer.valueOf(data);
    }
    public <T>T getObj(String key){
        return (T) map.get(key);
    }
    public void setAttr(String key, Object value){
        map.put(key, value);
    }
    public boolean empty(){
        return map.isEmpty();
    }
}
