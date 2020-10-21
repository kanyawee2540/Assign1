package com.example.assignment1;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(int sales, double sharePercentage, int salesMinusShare, int salesShare) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SALES, sales);
        contentValue.put(DatabaseHelper.SHARE_PERCENTAGE, sharePercentage);
        contentValue.put(DatabaseHelper.SALES_MINUS_SHARE, salesMinusShare);
        contentValue.put(DatabaseHelper.SALES_SHARE, salesShare);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[]{DatabaseHelper._ID,
                DatabaseHelper.SALES,
                DatabaseHelper.SHARE_PERCENTAGE,
                DatabaseHelper.SALES_MINUS_SHARE,
                DatabaseHelper.SALES_SHARE,
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, int sales, double sharePercentage, int salesMinusShare, int salesShare) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SALES, sales);
        contentValues.put(DatabaseHelper.SHARE_PERCENTAGE, sharePercentage);
        contentValues.put(DatabaseHelper.SALES_MINUS_SHARE, salesMinusShare);
        contentValues.put(DatabaseHelper.SALES_SHARE, salesShare);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
