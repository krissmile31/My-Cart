package com.hanu.a2_1801040015.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanu.a2_1801040015.MainActivity;
import com.hanu.a2_1801040015.ProductDetail;
import com.hanu.a2_1801040015.R;
import com.hanu.a2_1801040015.adapters.animate.CircleAnimationUtil;
import com.hanu.a2_1801040015.database.ProductManager;
import com.hanu.a2_1801040015.models.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.hanu.a2_1801040015.MainActivity.emptyResult;
import static com.hanu.a2_1801040015.MainActivity.emptySearch;
import static com.hanu.a2_1801040015.MainActivity.tvNumber;
import static com.hanu.a2_1801040015.ProductDetail.carts;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Filterable {
    private List<Product> productList;
    private List<Product> productFilter;
//    private OnClickListener listener;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        this.productFilter = productList;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View itemView = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new ProductHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filter = constraint.toString();
                if (filter.isEmpty()) {
                    productList = productFilter;
                    emptySearch.animate().alpha(0f);
                    emptyResult.animate().alpha(0f);
                }
                else {
                    List<Product> searchList = new ArrayList<>();
                    for (Product product: productFilter){
                        if (product.getName().toLowerCase().contains(filter.toLowerCase()))
                            searchList.add(product);
                        else {
                            emptySearch.animate().alpha(1f);
                            emptyResult.animate().alpha(1f);
//                            emptyResult.setText(filter);
                        }
                    }
                    productList = searchList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values;
                if (productList.isEmpty()) {
                    emptySearch.animate().alpha(1f);
                    emptyResult.setText(constraint);
                }
                else {
                    emptySearch.animate().alpha(0f);
                    emptyResult.animate().alpha(0f);

                }

                notifyDataSetChanged();
            }
        };
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        private Context context;
        private ImageView thumbnail;
        private TextView name, unitPrice, category;

        private ImageView cart, badge, search;
        private ImageView imageView;

        public ProductHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.name);
            category = itemView.findViewById(R.id.category);
            unitPrice = itemView.findViewById(R.id.unitPrice);

            cart = itemView.findViewById(R.id.cart);
            badge = itemView.findViewById(R.id.icon_badge);
            search = itemView.findViewById(R.id.search);
        }

        public void bind(Product product) {

            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(product.getThumbnail());
            name.setText(product.getName());
            double price = product.getUnitPrice();
            unitPrice.setText("₫ " + NumberFormat.getNumberInstance(Locale.GERMAN).format(price));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetail.quantity = product.getQuantity();

                    Intent intent = new Intent(context, ProductDetail.class);
                    intent.putExtra("detail", product);

//                    Bundle extras = new Bundle();
//                    extras.putString("itemName", get_product.getName());
//                    extras.putString("itemCategory", get_product.getCategory());
//                    extras.putString("itemPrice", NumberFormat.getNumberInstance(Locale.GERMAN).format(get_product.getUnitPrice()));
//                    intent.putExtra("ExtraBundle", extras);
                    context.startActivity(intent);
                }
            });


//            imageView.setImageBitmap();
            itemView.findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    quantity++;
//                    product.setQuantity(1);
                    Log.i("quantity: ", String.valueOf(product.getQuantity()));
                    carts.add(product);
                    ProductManager productManager = ProductManager.getInstance(context);

//                    ProductDetail.quantity = Integer.parseInt(String.valueOf(CartActivity.quantity_cart));
//                    MainActivity.cart_count++;
                    for (int i = 0; i < carts.size(); i++) {
                        for (int j = i + 1; j < carts.size(); j++) {
                            if (carts.get(i).getName().equals(carts.get(j).getName())) {
                                carts.get(i).setQuantity(carts.get(i).getQuantity() + 1);
                                carts.remove(j);
                                j--;
//                                 productManager.delete(carts.get(i).getId());
                                productManager.update(carts.get(i));
                            }
                        }
                    }

                    productManager.add(product);
//                    MainActivity.cart_count = 1;

//                    Product a = new Product(product.getThumbnail(), product.getName(), product.getUnitPrice(), product.getQuantity());
//                    int[] location=new int[2];
//                    v.getLocationInWindow(location);
//                    location[0]-=100;
//                    listener.onClick(getAdapterPosition(), a, location);

                    CircleAnimationUtil circleAnimationUtil = new CircleAnimationUtil();
                    circleAnimationUtil.attachActivity((Activity) context)
                    .setTargetView(thumbnail)
                    .setDestView(tvNumber)
//                    .setBorderWidth(1)
//                    .setBorderColor(Color.WHITE)
                    .setAnimationListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            tvNumber.animate().alpha(1f);
                            tvNumber.setText(String.valueOf(++MainActivity.count));
                            MainActivity.updateShopingCart(1);
//                            Animation aniAdd = AnimationUtils.loadAnimation(context, R.anim.fade_out_animation);
////                            animation.setDuration(3000);
//                            CircleAnimationUtil.mImageView.startAnimation(aniAdd);
                            super.onAnimationEnd(animation);
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
//                            thumbnail.setVisibility(View.VISIBLE);
                            super.onAnimationStart(animation);
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            tvNumber.setText(String.valueOf(++MainActivity.count));
                            MainActivity.updateShopingCart(1);
                            super.onAnimationRepeat(animation);
                        }

                        @Override
                        public void onAnimationResume(Animator animation) {
                            tvNumber.setText(String.valueOf(++MainActivity.count));
                            MainActivity.updateShopingCart(1);
                            super.onAnimationResume(animation);
                        }
                    }).startAnimation();
                }
            });
        }

        // Using AsyncTask
        public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                thumbnail.setImageBitmap(bitmap);
            }
        }
    }

//    public static void setDataIndex(int index,Product product){
//        if (product==null){
//            return;
//        }
//        if (productList==null){
//            productList = new ArrayList<>();
//        }
//        productList.set(index,product);
////            notifyItemChanged(getHeaderItemCount() + index);
//    }
//
////    public interface OnClickListener {
//        void onClick(int position, Product product, int[] location);
//    }
//
//    public void setOnClickListener (OnClickListener listener) {
//        this.listener = listener;
//    }

}
