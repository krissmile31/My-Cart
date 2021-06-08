package com.hanu.a2_1801040015;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanu.a2_1801040015.cart.CartActivity;
import com.hanu.a2_1801040015.database.ProductManager;
import com.hanu.a2_1801040015.models.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.hanu.a2_1801040015.MainActivity.tvNumber;

public class ProductDetail extends AppCompatActivity {
    private ImageView thumbnail;
    private TextView name, price, quantityInfo;
    private int basePrice;
    public static int quantity;
    private Product product;
    private Button addToCart;
    public static List<Product> carts = new ArrayList<>();;
    private ProductManager productManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        thumbnail = findViewById(R.id.thumbnailInfo);
        name = findViewById(R.id.nameInfo);
        price = findViewById(R.id.priceInfo);
        quantityInfo = findViewById(R.id.quantityInfo);
        addToCart = findViewById(R.id.addtocart);

        productManager = ProductManager.getInstance(this);

        Intent intent = getIntent();
        if (intent != null){
            product = (Product) intent.getSerializableExtra("detail");
//            quantityInfo.setText(String.valueOf(product.getQuantity()));
            Picasso.with(this).load(product.getThumbnail()).into(thumbnail);
            name.setText(product.getName());
            basePrice = (int)product.getUnitPrice();
            price.setText("₫ " + NumberFormat.getNumberInstance(Locale.GERMAN).format(basePrice));
        }
//        Bundle extras = getIntent().getBundleExtra("ExtraBundle");
//
//        product = (Product) getIntent().getSerializableExtra("ExtraBundle");
//        name.setText(extras.getString("itemName"));
//        category.setText("Category: " + extras.getString("itemCategory"));
//        price.setText(extras.getString("itemPrice"));

        quantityInfo.setText(String.valueOf(product.getQuantity()));
        Log.i("G", String.valueOf(product.getQuantity()));
        quantity = Integer.parseInt(quantityInfo.getText().toString());

        findViewById(R.id.addquantity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                product.setQuantity(quantity);
                Log.i("quantity: ", String.valueOf(product.getQuantity()));
//
                quantityInfo.setText(String.valueOf(quantity));

                int sumPrice = basePrice * quantity;
                price.setText("₫ " + NumberFormat.getNumberInstance(Locale.GERMAN).format(sumPrice));
            }
        });

        findViewById(R.id.subquantity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity == 1)
                    Toast.makeText(ProductDetail.this, "The product quantity equals 0", Toast.LENGTH_SHORT).show();
                else {
                    quantity--;
                    product.setQuantity(quantity);
                    Log.i("quantity: ", String.valueOf(product.getQuantity()));

                    quantityInfo.setText(String.valueOf(quantity));

                    int sumPrice = basePrice * quantity;
                    price.setText("₫ " + NumberFormat.getNumberInstance(Locale.GERMAN).format(sumPrice));

                }
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProductDetail.this, CartActivity.class);
                carts.add(product);
                tvNumber.animate().alpha(1f);

                for (int i = 0; i < carts.size(); i++) {
                    for (int j = i + 1; j < carts.size(); j++) {
                        if (carts.get(i).getName().equals(carts.get(j).getName())) {
                            carts.get(i).setQuantity(carts.get(j).getQuantity());
                            carts.remove(j);
                            j--;
                            productManager.update(ProductDetail.carts.get(i));
//                            productManager.delete(ProductDetail.carts.get(j).getId());

                        }
                    }
                }

                productManager.add(product);

                startActivity(intent1);
            }
        });
        addToCart.setEnabled(quantity > 0);

    }
}