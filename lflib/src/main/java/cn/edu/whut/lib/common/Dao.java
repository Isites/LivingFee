package cn.edu.whut.lib.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.edu.whut.lib.sqlite.DBHelper;

public abstract class Dao {

    public static final int INT_NULL = -1;

    protected SQLiteDatabase dao;

    public Dao(){
        //默认使用可读写的dao
        dao = DBHelper.openWrite();
    }
    public Cursor query(String sql){
        return dao.rawQuery(sql, null);
    }
    public long insert(String tableName, ContentPut contentPut){
        ContentValues values = new ContentValues();
        contentPut.put(values);
        if(values.size() > 0){
            return dao.insert(tableName, null, values);
        }
        return -1;
    }
    public int update(String tableName, ContentPut contentPut, String where, String[] whereArgs){
        ContentValues values = new ContentValues();
        contentPut.put(values);
        if(values.size() > 0){
            return dao.update(tableName, values, where, whereArgs);
        }
        return -1;
    }
    public int delete(String tableName, String where, String[] whereArgs){
        return dao.delete(tableName, where, whereArgs);
    }
    public void delete(String sql){
        dao.execSQL(sql);
    }

    protected List<DataResult> rows2Data(Cursor cursor){
        List<DataResult> list = new ArrayList<DataResult>();
        while(cursor.moveToNext()){

            DataResult result = row2Data(cursor);
            list.add(result);
        }
        return list;
    }
    protected DataResult row2Data(Cursor cursor){
        DataResult result = new DataResult();
        for(int i = 0; i < cursor.getColumnCount(); i++){
            result.setAttr(cursor.getColumnName(i), getValue(cursor, i));
        }
        return result;
    }
    protected Object getValue(Cursor cursor, int index){
        int type = cursor.getType(index);
        Object obj = null;
        switch (type){
            case Cursor.FIELD_TYPE_FLOAT:
                obj = cursor.getFloat(index);
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                obj = cursor.getInt(index);
                break;
            case Cursor.FIELD_TYPE_STRING:
                obj = cursor.getString(index);
                break;
            case Cursor.FIELD_TYPE_NULL:
                obj = null;
            default:
        }
        return obj;
    }
    public SQLiteDatabase getDao() {
        return dao;
    }
    public void setDao(SQLiteDatabase dao) {
        this.dao = dao;
    }
}
