package cn.edu.whut.lib.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "livingfee";
    private static final int DB_VERSION = 1;
    //默认 rowid自增
    private static final String INIT_ACCOUNTS =
            "CREATE TABLE IF NOT EXISTS `accounts` (`id` INTEGER  primary key autoincrement,\n" +
            "  `account_name` char(50) NOT NULL DEFAULT '0' ,\n" +
            "  `account_desc` char(50) DEFAULT '0' ,\n" +
            "  `total_fee` float DEFAULT '0' \n" +
            ") ;\n" ;
    private static final String INIT_BLOG =
            "CREATE TABLE IF NOT EXISTS `blog` (`id` INTEGER  primary key autoincrement,\n" +
            "  `content` text NOT NULL,\n" +
            "  `fee_id` int(11) DEFAULT '0' ,\n" +
            "  `update_time` datetime ,\n" +
            "  `create_time` datetime \n" +
            ") ;\n";
    private static final String INIT_BUDGET =
            "CREATE TABLE IF NOT EXISTS `budget` (`id` INTEGER  primary key autoincrement,\n" +
            "  `user_id` int(11) NOT NULL DEFAULT '0',\n" +
            "  `day_budget` float NOT NULL DEFAULT '0' ,\n" +
            "  `mouth_budget` float NOT NULL DEFAULT '0' ,\n" +
            "  `mouth_alert` float NOT NULL DEFAULT '0' \n" +
            ") ;\n";
    private static final String INIT_FEES =
            "CREATE TABLE IF NOT EXISTS `in_out_fee` (`id` INTEGER  primary key autoincrement,\n" +
            "  `type` int(2) NOT NULL ,\n" +
            "  `description` char(50) DEFAULT NULL,\n" +
            "  `fee` float NOT NULL,\n" +
            "  `blog_id` int(11) DEFAULT NULL,\n" +
            "  `user_id` int(11) NOT NULL,\n"+
            "  `spend_id` int(11) DEFAULT NULL,\n"+
            "  `account_id` int(11) DEFAULT NULL,\n" +
            "  `update_time` datetime ,\n" +
            "  `create_time` datetime \n" +
            ") ;\n";
    private static final String INIT_SPENDTYPE =
            "CREATE TABLE IF NOT EXISTS `spend_type` (`id` INTEGER  primary key autoincrement,\n" +
            "  `resource` char(50) NOT NULL DEFAULT '0',\n" +
            "  `parent_id` int(11) NOT NULL DEFAULT '0' ,\n" +
            "  `name` char(8) NOT NULL DEFAULT '0',\n" +
            "  `description` char(50) NOT NULL DEFAULT '0'\n" +
            ")  ;\n";
    private static final String INIT_USER =
            "CREATE TABLE IF NOT EXISTS `user` (`id` INTEGER  primary key autoincrement,\n" +
            "  `user_name` char(50) NOT NULL DEFAULT '0',\n" +
            "  `user_pass` char(10) NOT NULL DEFAULT '0',\n" +
            "  `create_time` datetime \n" +
            ") ;\n";

    private SQLiteDatabase readCon;
    private SQLiteDatabase writeCon;
    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INIT_ACCOUNTS);
        db.execSQL(INIT_BLOG);
        db.execSQL(INIT_BUDGET);
        db.execSQL(INIT_FEES);
        db.execSQL(INIT_USER);
        db.execSQL(INIT_SPENDTYPE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public SQLiteDatabase getReadCon(){
        if(readCon == null || !readCon.isOpen()){
            readCon = getReadableDatabase();
        }
        return readCon;
    }
    public SQLiteDatabase getWriteCon(){
        if(writeCon == null || !writeCon.isOpen()){
            writeCon = getWritableDatabase();
        }
        return writeCon;
    }
}
