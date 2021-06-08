package com.hanu.a2_1801040015;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.hanu.a2_1801040015.adapters.ProductAdapter;
import com.hanu.a2_1801040015.cart.CartActivity;
import com.hanu.a2_1801040015.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> products;
    private EditText searchView;
    private ImageView search;
    private ImageView thumbnail;
    private TextView name, unitPrice;
    private Toolbar toolbar;

    private ImageView imageView;
    public static TextView tvNumber;

    public static int count = 0;
    public static  TextView emptySearch, emptyResult;
    public static int quantity;
    private static final String PRODUCT_URL = "https://mpr-cart-api.herokuapp.com/products/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thumbnail = findViewById(R.id.thumbnail);

//        getSupportActionBar().hide();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tvNumber = findViewById(R.id.number);
        count = ProductDetail.carts.size();
        if (count != 0) {
            tvNumber.setText(String.valueOf(count));
            tvNumber.animate().alpha(1f);
        }
//        else {
//            tvNumber.animate().alpha(1f);
//
//        }

        name = findViewById(R.id.name);
        unitPrice = findViewById(R.id.unitPrice_cart);
        recyclerView = findViewById(R.id.recycleViewProduct);
        searchView = findViewById(R.id.searchView);
        search = findViewById(R.id.search);
        emptySearch = findViewById(R.id.emptySearch);
        emptyResult = findViewById(R.id.emptyResult);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        products = new ArrayList<>();
        setProductList();

        productAdapter = new ProductAdapter(products);
        recyclerView.setAdapter(productAdapter);

        // tool bar
//        productAdapter.setOnClickListener(new ProductAdapter.OnClickListener() {
//            @Override
//            public void onClick(int position, Product product, int[] location) {
//                productAdapter.setDataIndex(position, product);
//                showAnimation(location);
//
////                makeFlyAnimation(search);
//            }
//        });
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // search product
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Searching...");
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);

//                ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
//                progressBar.animate().alpha(1f);
//                Sprite doubleBounce = new WanderingCubes();
//                progressBar.setIndeterminateDrawable(doubleBounce);
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        progressBar.cancelLongPress();
//                    }
//                }, 1000);
            }
        });

        findViewById(R.id.shopping_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
    }

    // parse JSON
    private void setProductList() {
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading data...");
//        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, PRODUCT_URL,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        progressDialog.dismiss();
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                Product product = new Product();

                                product.setThumbnail(object.getString("thumbnail"));
                                product.setName(object.getString("name"));
                                product.setUnitPrice(object.getDouble("unitPrice"));
                                product.setQuantity(1); // default quantity = 1 when add click

                                products.add(product);
                            }
                            productAdapter = new ProductAdapter(products);
                            recyclerView.setAdapter(productAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Stupid", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_cart, menu);
//        MenuItem menuItem = menu.findItem(R.id.shopping_cart);
//        MainActivity.cart_count = ProductDetail.carts.size();
//
//        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.carts));
//
//        return super.onCreateOptionsMenu(menu);
//    }
//

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.shopping_cart:
//
//                    startActivity(new Intent(this, CartActivity.class));
//
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//        return true;
//    }

    @Override
    protected void onStart() {
        super.onStart();
        invalidateOptionsMenu();
    }
//
//    private void addItemToCart() {
//        TextView textView = (TextView) findViewById(R.id.textNotify);
//        textView.setText(String.valueOf(++aa));
//    }
//
//    private void makeFlyAnimation(ImageView targetView) {
//
//        RelativeLayout destView = (RelativeLayout) findViewById(R.id.cartRelativeLayout);
//
//        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(1000).setDestView(destView).setAnimationListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                addItemToCart();
//                Toast.makeText(MainActivity.this, "Continue Shopping...", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        }).startAnimation();
//
//
//    }


    public static void updateShopingCart(int numberItems) {

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(tvNumber, "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(tvNumber, "scaleY", 0.2f, 1.0f);

        scaleDownX.setDuration(500);
        scaleDownY.setDuration(500);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.setInterpolator(new OvershootInterpolator());
        scaleDown.play(scaleDownX).with(scaleDownY);

        scaleDown.start();
    }

//    public void showAnimation(int[] startLocation) {
//
//        int[] endLocation = new int[2];
//        tvNumber.getLocationInWindow(endLocation);
//
//        // 计算位移
//        int endX = -startLocation[0] + 1000 ;// 动画位移的X坐标
//
//        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标
//        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
//                endX, 0, 0);
//        translateAnimationX.setInterpolator(new LinearInterpolator());
//        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
//        translateAnimationX.setFillAfter(true);
//
//        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
//                0, endY);
//        translateAnimationY.setInterpolator(new AccelerateInterpolator());
//        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
//        translateAnimationX.setFillAfter(true);
//
//        final AnimationSet set = new AnimationSet(false);
//        set.setFillAfter(false);
//        set.addAnimation(translateAnimationY);
//        set.addAnimation(translateAnimationX);
//        set.setDuration(800);// 动画的执行时间
//
//        ImageView imageView = new ImageView(this);
//        imageView.setImageResource(R.drawable.ball);
//
//        final ViewGroup anim_mask_layout = createAnimLayout();
//        anim_mask_layout.addView(imageView);//把动画小球添加到动画层
//        final View view = addViewToAnimLayout(anim_mask_layout, imageView,
//                startLocation);
//        view.startAnimation(set);
//        // 动画监听事件
//
//        count--;
//        Log.i("count ", String.valueOf(count));
//        set.setAnimationListener(new Animation.AnimationListener() {
//            // 动画的开始
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // TODO Auto-generated method stub
//            }
//
//            // 动画的结束
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                Log.i("count ", String.valueOf(count));
//                anim_mask_layout.removeAllViews();
//                set.cancel();
//                animation.cancel();
//                tvNumber.setText(String.valueOf(++count));
//                updateShopingCart(1);
//            }
//        });
//
//
//    }
//
//    private ViewGroup createAnimLayout() {
//        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
//        LinearLayout animLayout = new LinearLayout(this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        animLayout.setLayoutParams(lp);
//        animLayout.setId(Integer.MAX_VALUE);
//        animLayout.setBackgroundResource(android.R.color.transparent);
//        rootView.addView(animLayout);
//        return animLayout;
//    }
//
//    private View addViewToAnimLayout(final ViewGroup parent, final View view,
//                                     int[] location) {
//        int x = location[0];
//        int y = location[1];
////        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
////                LinearLayout.LayoutParams.WRAP_CONTENT,
////                LinearLayout.LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                50,
//                50);
//        lp.leftMargin = x;
//        lp.topMargin = y;
//        view.setLayoutParams(lp);
//        return view;
//    }
}
