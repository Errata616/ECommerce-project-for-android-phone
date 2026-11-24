package com.example.bt1;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {
    ImageView image;
    TextView name;
    TextView description;
    TextView specification;
    TextView price;
    Button backButton;
    Button buyNow;
    Button addToCart;
    Button cartHeader;
    RecyclerView relatedRecyclerView;
    ArrayList<Product> relatedProductsList = new ArrayList<>();
    ProductAdapter relatedAdapter;
    FirebaseFirestore fireDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_product_detail);

        fireDB = FirebaseFirestore.getInstance();

        image = findViewById(R.id.detailImage);
        name = findViewById(R.id.detailName);
        description = findViewById(R.id.detailDescription);
        specification = findViewById(R.id.detailSpecification);
        price = findViewById(R.id.detailPrice);
        backButton = findViewById(R.id.detailBackButton);
        buyNow = findViewById(R.id.buyNow);
        addToCart = findViewById(R.id.addToCart);
        cartHeader = findViewById(R.id.cartHeader);
        relatedRecyclerView = findViewById(R.id.relatedRecyclerView);

        relatedRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        relatedAdapter = new ProductAdapter(this, relatedProductsList);
        relatedRecyclerView.setAdapter(relatedAdapter);

        String productName = getIntent().getStringExtra(Constants.KEY_PRODUCT_NAME);
        int productImage = getIntent().getIntExtra(Constants.KEY_PRODUCT_IMAGE, 0);
        String productDescription = getIntent().getStringExtra(Constants.KEY_PRODUCT_DESCRIPTION);
        String productSpecification = getIntent().getStringExtra(Constants.KEY_PRODUCT_SPECIFICATION);
        int productPrice = getIntent().getIntExtra(Constants.KEY_PRODUCT_PRICE, 0);

        if (productName != null) name.setText(productName);
        if (productImage != 0) image.setImageResource(productImage);
        if (productDescription != null) description.setText(productDescription);

        String title = "Thông số kỹ thuật:\n";
        SpannableString spannable;
        if (productSpecification != null && !productSpecification.isEmpty()) {
            spannable = new SpannableString(title + productSpecification);
        }

        else {
            spannable = new SpannableString(title + "đang cập nhật");
        }

        spannable.setSpan(
                new StyleSpan(Typeface.BOLD),
                0,
                title.length() - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannable.setSpan(
                new ForegroundColorSpan(Color.parseColor("#F04A13")),
                0,
                title.length() - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        specification.setText(spannable);
        price.setText(String.format("%,d đ", productPrice));

        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (buyNow != null) {
            buyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent buyNowIntent = new Intent(ProductDetailActivity.this, BuyNow.class);
                    buyNowIntent.putExtra(Constants.KEY_PRODUCT_NAME, productName);
                    buyNowIntent.putExtra(Constants.KEY_PRODUCT_IMAGE, productImage);
                    buyNowIntent.putExtra(Constants.KEY_PRODUCT_PRICE, productPrice);
                    startActivity(buyNowIntent);
                }
            });
        }

        if (addToCart != null) {
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartManager.getInstance().addProductToCart(productName, productPrice, productImage);
                    Toast.makeText(ProductDetailActivity.this, "Đã thêm " + productName + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (cartHeader != null) {
            cartHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cartRedirect = new Intent(ProductDetailActivity.this, BuyInCart.class);
                    startActivity(cartRedirect);
                }
            });
        }

        loadRelatedProductsFromFirestore();
    }

    private void loadRelatedProductsFromFirestore() {
        fireDB.collection("products").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        relatedProductsList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String prodName = document.getString("name");
                            Long priceLong = document.getLong("price");
                            int prodPrice = priceLong != null ? priceLong.intValue() : 0;
                            String prodDesc = document.getString("description");
                            String prodSpec = document.getString("specification");
                            String category = document.getString("category");

                            String imgName = document.getString("imageName");
                            int prodImage = R.drawable.arduino;
                            if (imgName != null && !imgName.isEmpty()) {
                                int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());
                                if (resId != 0) {
                                    prodImage = resId;
                                }
                            }

                            relatedProductsList.add(new Product(prodName, prodPrice, prodDesc, prodSpec, category, prodImage));
                        }
                        relatedAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FirestoreError", "Lỗi tải sản phẩm liên quan: ", e);
                    }
                });
    }
}
