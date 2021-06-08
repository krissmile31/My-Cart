package com.hanu.a2_1801040015.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hanu.a2_1801040015.MainActivity;
import com.hanu.a2_1801040015.ProductDetail;
import com.hanu.a2_1801040015.R;
import com.hanu.a2_1801040015.database.ProductManager;
import com.hanu.a2_1801040015.models.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    public static List<Product> cartList;
    private OnClickListener listener;

    public CartAdapter(List<Product> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartHolder holder, int position) {
        holder.bind(cartList.get(position));

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder{
        private Context context;
        private ImageView thumbnail_cart, minus, plus;
        private TextView name_cart, price_cart, unitPrice_cart, quantity_cart;
        private int quantity;
        private double price;

        public CartHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            thumbnail_cart = itemView.findViewById(R.id.thumbnail_cart);
            minus = itemView.findViewById(R.id.minus);
            plus = itemView.findViewById(R.id.plus);
            name_cart = itemView.findViewById(R.id.name_cart);
            price_cart = itemView.findViewById(R.id.price_cart);
            unitPrice_cart = itemView.findViewById(R.id.unitPrice_cart);
            quantity_cart = itemView.findViewById(R.id.quantity);
        }

        public void bind(Product product) {

            Picasso.with(context).load(product.getThumbnail()).into(thumbnail_cart);
            name_cart.setText(product.getName());
//            unitPrice_cart.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(product.getUnitPrice()));
            price_cart.setText("₫ " + NumberFormat.getNumberInstance(Locale.GERMAN).format(product.getUnitPrice()));

            quantity_cart.setText(String.valueOf(product.getQuantity()));

            quantity = Integer.parseInt(quantity_cart.getText().toString());
            price = product.getUnitPrice();

            int sumPrice = (int)price * quantity;
            unitPrice_cart.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(sumPrice));

            MainActivity.count = cartList.size();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProductDetail.class);
                    intent.putExtra("detail", product);
                    context.startActivity(intent);                }
            });

            quantity_cart.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (quantity_cart.getText().toString().isEmpty()){
                        beforeTextChanged(s, start, before,count);
                        return;
                    }
                    quantity = Integer.parseInt(quantity_cart.getText().toString());
                    product.setQuantity(quantity);
                    
                    ProductManager.getInstance(context).update(product);

                    int sumPrice = (int)price * quantity;
                    unitPrice_cart.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(sumPrice));
                    listener.onClick(product);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity++;
                    product.setQuantity(quantity);
                    ProductManager.getInstance(context).update(product);
                    quantity_cart.setText(String.valueOf(quantity));

                    int sumPrice =(int) price * quantity;
                    unitPrice_cart.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(sumPrice));
                    listener.onClick(product);
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantity == 1) {
                        product.setQuantity(0);
                        Toast.makeText(context, "Remove", Toast.LENGTH_SHORT).show();
                        cartList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), cartList.size());
                        ProductManager productManager = ProductManager.getInstance(context);
                        productManager.delete(product.getId());

                    } else {
                        quantity--;
                        product.setQuantity(quantity);
                        ProductManager.getInstance(context).update(product);

                        quantity_cart.setText(String.valueOf(quantity));

                        int sumPrice = (int) price * quantity;
                        unitPrice_cart.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(sumPrice));
                        listener.onClick(product);
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void onClick(Product product);
    }

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }


}
