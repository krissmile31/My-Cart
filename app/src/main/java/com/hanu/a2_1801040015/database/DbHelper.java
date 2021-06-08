package com.hanu.a2_1801040015.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    private Context mContext;
    private static final String DATABASE_NAME = "carts.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DbSchema.ProductsTable.NAME + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbSchema.ProductsTable.Cols.THUMBNAIL + " TEXT, " +
                DbSchema.ProductsTable.Cols.NAME + " TEXT, " +
                DbSchema.ProductsTable.Cols.PRICE + " DOUBLE, " +
                DbSchema.ProductsTable.Cols.QUANTITY + " INTEGER" + ")");

        // other tables here
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("My Friends", "My Friends: upgrading DB; dropping/recreating tables.");
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.ProductsTable.NAME);

        // other tables here

        onCreate(db);
    }
}
