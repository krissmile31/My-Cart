package com.hanu.a2_1801040015.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.hanu.a2_1801040015.models.Product;

import java.util.List;

public class ProductManager {
    // singleton
    private static ProductManager instance;

    private static final String INSERT_STMT =
            "INSERT INTO " + DbSchema.ProductsTable.NAME + "(thumbnail, name, price, quantity) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_STMT =
            "UPDATE " + DbSchema.ProductsTable.NAME + " SET " + DbSchema.ProductsTable.Cols.QUANTITY + "= ? WHERE id = ?";
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public static ProductManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductManager(context);
        }

        return instance;
    }

    private ProductManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    public List<Product> all() {
        String sql = "SELECT * FROM " + DbSchema.ProductsTable.NAME + " GROUP BY " + DbSchema.ProductsTable.Cols.NAME;
        Cursor cursor = db.rawQuery(sql, null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);

        return cursorWrapper.getProducts();
    }

    /**
     * @modifies friend
     */
    public boolean add(Product product) {
        SQLiteStatement statement = db.compileStatement(INSERT_STMT);

        statement.bindString(1, product.getThumbnail());
        statement.bindString(2, product.getName());
        statement.bindDouble(3, product.getUnitPrice());
        statement.bindLong(4, ((long) (product.getQuantity())));

        long id = statement.executeInsert();
        // a
        if (id > 0) {
            product.setId(id);
            return true;
        }

        return false;
    }

    public boolean update (Product product) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.ProductsTable.Cols.QUANTITY, product.getQuantity());
        long result = db.update(DbSchema.ProductsTable.NAME, contentValues, "id=?", new String[]{product.getId() + ""});
        return result > 0;

    }

    public boolean delete(long id) {
        int result = db.delete(DbSchema.ProductsTable.NAME, "id = ?", new String[]{ id+"" });

        return result > 0;
    }

    public void clear() {
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DbSchema.ProductsTable.NAME);
    }

}