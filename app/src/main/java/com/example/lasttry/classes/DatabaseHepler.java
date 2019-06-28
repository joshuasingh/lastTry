package com.example.lasttry.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHepler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "lasttry.db";
    public static final String TABLE_NAME = "contact";
    public static final String ID = "id";
    public static final String COLUMN_NAME_NO = "contact_no";
    public static final String COLUMN_NAME_NAME = "contact_name";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER  PRIMARY KEY autoincrement," +
            COLUMN_NAME_NO + " TEXT," +
            COLUMN_NAME_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public DatabaseHepler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }


    public boolean insertData(String no, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_NO, no);
        contentValues.put(COLUMN_NAME_NAME, name);
        long res = db.insert(TABLE_NAME, null, contentValues);
        if (res == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);

         return res;
    }

}
