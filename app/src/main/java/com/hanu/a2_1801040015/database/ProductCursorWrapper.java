package com.hanu.a2_1801040015.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.hanu.a2_1801040015.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductCursorWrapper extends CursorWrapper {
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct() {
        long id = getLong(getColumnIndex("id"));
        String thumbnail = getString(getColumnIndex(DbSchema.ProductsTable.Cols.THUMBNAIL));
        String name = getString(getColumnIndex(DbSchema.ProductsTable.Cols.NAME));
        double price = getDouble(getColumnIndex(DbSchema.ProductsTable.Cols.PRICE));
        int quantity = getInt(getColumnIndex(DbSchema.ProductsTable.Cols.QUANTITY));


        Product product = new Product(id, thumbnail, name, price, quantity);

        return product;
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()) {
            Product product = getProduct();
            products.add(product);

            moveToNext();
        }

        return products;
    }
}
