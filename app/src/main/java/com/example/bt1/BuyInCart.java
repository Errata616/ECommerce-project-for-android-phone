package com.example.bt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class BuyInCart extends AppCompatActivity {
    ListView cartListView;
    TextView cartTotalPrice;
    TextView emptyCartText;
    Button cartCheckout;
    Button backCart;
    List<CartProduct> cartProductList;
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_add_to_card);

        cartListView = findViewById(R.id.cartListView);
        cartTotalPrice = findViewById(R.id.cartTotalPrice);
        emptyCartText = findViewById(R.id.emptyCartText);
        cartCheckout = findViewById(R.id.cartCheckout);
        backCart = findViewById(R.id.backCart);

        if (backCart != null) {
            backCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        cartProductList = CartManager.getInstance().getCartList();

        cartAdapter = new CartAdapter(this, cartProductList, new CartAdapter.OnCartChangeListener() {
            @Override
            public void onCartChanged() {
                updateTotalPrice();
            }
        });
        cartListView.setAdapter(cartAdapter);

        updateTotalPrice();

        if (cartCheckout != null) {
            cartCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cartProductList.isEmpty()) {
                        Toast.makeText(BuyInCart.this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        Intent checkoutIntent = new Intent(BuyInCart.this, BuyNow.class);
                        checkoutIntent.putExtra("name", "Đơn hàng (" + cartProductList.size() + " món)");
                        checkoutIntent.putExtra("price", calculateTotalAmount());
                        startActivity(checkoutIntent);
                    }
                }
            });
        }
        cartListView.setEmptyView(emptyCartText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cartAdapter != null) {
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        int total = calculateTotalAmount();
        if (cartTotalPrice != null) {
            cartTotalPrice.setText(String.format("%,d đ", total));
        }
    }

    private int calculateTotalAmount() {
        int total = 0;
        for (CartProduct item : cartProductList) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
