package com.example.spreadsheettest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String  DATABASE_NAME = "Accelerometer_recording.db";
    public static final String TABLE_NAME = "Activity";
    public static final String COL0 = "ID";
    public static final String COL1 = "TIME";
    public static final String COL2 = "x_acc";
    public static final String COL3 = "y_acc";
    public static final String COL4 = "z_acc";

    public DatabaseHelper(Context context) {super(context, DATABASE_NAME,null,1);}


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TIME TEXT, x_acc TEXT, y_acc TEXT, z_acc TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean addData (String time, String x_acc, String y_acc, String z_acc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, time);
        contentValues.put(COL2, x_acc);
        contentValues.put(COL3, y_acc);
        contentValues.put(COL4, z_acc);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        } else{
            return true;
        }
    }

    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return data;
    }
}
