package com.hanu.a2_1801040015.cart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.hanu.a2_1801040015.MainActivity;
import com.hanu.a2_1801040015.R;
import com.hanu.a2_1801040015.adapters.CartAdapter;
import com.hanu.a2_1801040015.database.ProductManager;
import com.hanu.a2_1801040015.models.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.hanu.a2_1801040015.ProductDetail.carts;
import static com.hanu.a2_1801040015.adapters.CartAdapter.cartList;

public class CartActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    private CartAdapter cartAdapter;
    private RecyclerView recyclerView;
    private TextView totalPrice;
    private ProductManager productManager;
    private Toolbar toolbar;
    private GoogleApiClient googleApiClient;
    private TextView quantity_cart;

    private String SiteKey = "6LceTwMbAAAAABhNzXJQULfWI1_V2A32oPyFuJ8F";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recycleViewCart);
        totalPrice = findViewById(R.id.totalPrice);
        quantity_cart = findViewById(R.id.quantity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.alphatoolbar).animate().alpha(0f);

        productManager = ProductManager.getInstance(this);
        carts = productManager.all();
        cartAdapter = new CartAdapter(carts);
        if (cartList == null)
            findViewById(R.id.something).animate().alpha(0f);
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int total = TotalPrice(carts);
        totalPrice.setText(NumberFormat.getInstance(Locale.GERMANY).format(total));

        cartAdapter.setOnClickListener(new CartAdapter.OnClickListener() {
            @Override
            public void onClick(Product product) {
                int total = TotalPrice(carts);
                totalPrice.setText(NumberFormat.getInstance(Locale.GERMANY).format(total));
            }
        });

        // I am not a Robot
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(CartActivity.this)
                .build();
        googleApiClient.connect();
        findViewById(R.id.checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, SiteKey)
                        .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                            @Override
                            public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
//                                Status status = recaptchaTokenResult.getStatus();
//                                if (status != null && status.isSuccess()) {
                                    toolbar.animate().alpha(0f);
                                    findViewById(R.id.cartActivity).animate().alpha(0f);
                                    findViewById(R.id.checkoutActivity).animate().alpha(1f).setDuration(8000).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            findViewById(R.id.checkoutActivity).animate().alpha(0f).withEndAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    findViewById(R.id.doneActivity).animate().alpha(1f);
                                                    findViewById(R.id.continueshopping).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            startActivity(new Intent(CartActivity.this, MainActivity.class));
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                    Toast.makeText(CartActivity.this, "Success", Toast.LENGTH_SHORT).show();

//                                }
//                                else {
//                                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                                }
                            }
                        });
            }
        });
    }

    // calculate total price
    private int TotalPrice(List<Product> items) {
        int grandTotal = 0;
        for(int i = 0 ; i < items.size(); i++) {

            grandTotal += items.get(i).getUnitPrice() * items.get(i).getQuantity();
        }
        return grandTotal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_clear, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.clear_cart:
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.unchecked)
                        .setTitle("Are you sure to delete all products in your cart?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                carts.clear();
                                productManager.clear();
                                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .show();
                return super.onOptionsItemSelected(item);
                
            case R.id.continue_shopping:
                startActivity(new Intent(CartActivity.this, MainActivity.class));

        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
