package com.example.siren4price.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by k_asano on 16/01/06.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    //database info
    public static final String DB_NAME = "siren4price.db";
    public static final int DB_VERSION = 1;

    //sql table name
    public static final String TABLE_ITEM = "item";

    //table columns
    public static final String KEY_ID = "id";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_NAME = "name";
    public static final String KEY_BUY = "buy";

    //sql statements
    public static final String CREATE_TABLE_ITEM = "create table " + TABLE_ITEM + " ("
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_CATEGORY + " integer not null, "
            + KEY_NAME + " text not null, "
            + KEY_BUY + " integer not null"
            + ")";

    public DBOpenHelper(Context c){
        super(c,DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_ITEM);
        onCreate(db);
    }
}
