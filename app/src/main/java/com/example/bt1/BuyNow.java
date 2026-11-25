package com.example.bt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BuyNow extends AppCompatActivity {
    LinearLayout checkoutProductsContainer;
    TextView checkoutTotalAmount;
    EditText buyNowCustomerName;
    EditText buyNowPhone;
    EditText buyNowAddress;
    Button confirmOrder;
    Button backCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_buy_now);

        checkoutProductsContainer = findViewById(R.id.checkoutProductsContainer);
        checkoutTotalAmount = findViewById(R.id.checkoutTotalAmount);
        buyNowCustomerName = findViewById(R.id.buyNowCustomerName);
        buyNowPhone = findViewById(R.id.buyNowPhone);
        buyNowAddress = findViewById(R.id.buyNowAddress);
        confirmOrder = findViewById(R.id.confirmOrder);
        backCheckout = findViewById(R.id.backCheckout);

        android.content.SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedName = pref.getString("registeredUsername", "");
        String savedPhone = pref.getString("registeredPhone", "");
        String savedAddress = pref.getString("registeredAddress", "");

        if (buyNowCustomerName != null) buyNowCustomerName.setText(savedName);
        if (buyNowPhone != null) buyNowPhone.setText(savedPhone);
        if (buyNowAddress != null) buyNowAddress.setText(savedAddress);

        List<CartProduct> productsToCheckout = new ArrayList<>();

        String singleProductName = getIntent().getStringExtra("name");
        int singleProductPrice = getIntent().getIntExtra("price", 0);
        int singleProductImage = getIntent().getIntExtra("image", 0);

        if (singleProductName != null && singleProductPrice > 0 && !singleProductName.startsWith("Đơn hàng")) {
            productsToCheckout.add(new CartProduct(singleProductName, singleProductPrice, 1, singleProductImage));
        }

        else {
            productsToCheckout.addAll(CartManager.getInstance().getCartList());
        }

        int totalAmount = 0;
        if (checkoutProductsContainer != null) {
            checkoutProductsContainer.removeAllViews();

            for (CartProduct item : productsToCheckout) {
                totalAmount += item.getTotalPrice();

                View itemView = LayoutInflater.from(this).inflate(R.layout.item_cart, checkoutProductsContainer, false);

                ImageView img = itemView.findViewById(R.id.cartProductImage);
                TextView tvName = itemView.findViewById(R.id.cartProductName);
                TextView tvPrice = itemView.findViewById(R.id.cartProductPrice);
                TextView cartQuantity = itemView.findViewById(R.id.cartQuantity);
                Button cartDecrease = itemView.findViewById(R.id.cartDecrease);
                Button cartIncrease = itemView.findViewById(R.id.cartIncrease);

                if (cartDecrease != null) cartDecrease.setVisibility(View.GONE);
                if (cartIncrease != null) cartIncrease.setVisibility(View.GONE);

                if (img != null) img.setImageResource(item.getProductImage());
                if (tvName != null) tvName.setText(item.getProductName());
                if (tvPrice != null) tvPrice.setText(String.format("%,d đ", item.getProductPrice()));
                if (cartQuantity != null) cartQuantity.setText("x" + item.getProductQuantity());

                checkoutProductsContainer.addView(itemView);
            }
        }

        if (checkoutTotalAmount != null) {
            checkoutTotalAmount.setText(String.format("%,d đ", totalAmount));
        }

        if (backCheckout != null) {
            backCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (confirmOrder != null) {
            final int finalTotalAmount = totalAmount;
            confirmOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = buyNowCustomerName != null ? buyNowCustomerName.getText().toString().trim() : "";
                    String phone = buyNowPhone != null ? buyNowPhone.getText().toString().trim() : "";
                    String address = buyNowAddress != null ? buyNowAddress.getText().toString().trim() : "";

                    if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                        Toast.makeText(BuyNow.this, "Vui lòng nhập đầy đủ thông tin giao hàng!", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        // Tạo và lưu đơn hàng vào OrderManager
                        String orderId = "DH" + System.currentTimeMillis();
                        String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());
                        OrderModel newOrder = new OrderModel(orderId, name, phone, address, finalTotalAmount, date);
                        OrderManager.getInstance().addOrder(newOrder);

                        if (singleProductName == null || singleProductName.startsWith("Đơn hàng")) {
                            CartManager.getInstance().clearCart();
                        }

                        Toast.makeText(BuyNow.this, "Đặt hàng thành công tổng cộng " + String.format("%,d đ", finalTotalAmount) + "! Đơn hàng sẽ giao đến " + address, Toast.LENGTH_LONG).show();

                        Intent mainIntent = new Intent(BuyNow.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                    }
                }
            });
        }
    }
}
