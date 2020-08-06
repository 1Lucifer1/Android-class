package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {

    // TODO 定义表结构和 SQL 语句常量
    private TodoContract() {
    }


    //建表语句
    public static final String SQL_CREATE_TODO =
            "CREATE TABLE IF NOT EXISTS " + TodoEntry.TABLE_NAME+"("+
            TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            TodoEntry.COLUMN_NAME_DATE + " TIMEDATE DEFAULT CURRENT_TIMESTAMP,"+
            TodoEntry.COLUME_NAME_STATE + " INTEGER DEFAULT 0,"+
            TodoEntry.COLUMN_NAME_PRIORITY + "INTEGER DEFAULT 0,"+
            TodoEntry.COLUMN_NAME_CONTENT + " TEXT DEFAULT '')";


    //删表语句
    public static final String SQL_DELETE_TODO =
            "DROP TABLE IF EXISTS " + TodoEntry.TABLE_NAME;

    public static class TodoEntry implements BaseColumns{
        public static final String TABLE_NAME = "TODO";//表名
        public static final String COLUMN_NAME_DATE = "DATE";//列名
        public static final String COLUME_NAME_STATE = "STATE";//列名
        public static final String COLUMN_NAME_CONTENT = "CONTENT";//列名
        public static final String COLUMN_NAME_PRIORITY = "PRIORITY";
    }


}
