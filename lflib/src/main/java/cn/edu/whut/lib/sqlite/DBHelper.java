package cn.edu.whut.lib.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
    private static DBManager manager;
    private static Context context;

    public static void init(Context context){
        DBHelper.context = context;
    }

    private static DBManager getInstance(Context context){
        if(manager == null){
            synchronized (DBHelper.class){
                manager = new DBManager(context);
            }
        }
        return manager;
    }
    public static SQLiteDatabase openRead(){
        manager = getInstance(context);
        return manager.getReadCon();
    }
    public static SQLiteDatabase openWrite(){
        manager = getInstance(context);
        return manager.getWriteCon();
    }
}
