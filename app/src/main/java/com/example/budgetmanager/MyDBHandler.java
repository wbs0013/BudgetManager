package com.example.budgetmanager;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.Nullable;

import static android.content.Context.*;

public class MyDBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "finalBudget.db";
    private static final String TABLE_NAME = "finalBudget";

    private static final String COLUMN_1 = "[date]";
    private static final String COLUMN_2 = "amount";
    private static final String COLUMN_3 = "category";

    public MyDBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db = SQLiteDatabase.openOrCreateDatabase("budget.db", null);
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ExchangeID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_1
            + " TEXT, " + COLUMN_2 + " TEXT, " + COLUMN_3 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String date, String amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_1,date);
        contentValues.put(COLUMN_2,amount);
        contentValues.put(COLUMN_3,category);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from finalBudget", null);
        return cursor;
    }
}
