package com.example.bt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {
    Button cartHeader;
    Button backAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_about_us);

        backAboutUs = findViewById(R.id.backAboutUs);
        if (backAboutUs != null) {
            backAboutUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        cartHeader = findViewById(R.id.cartHeader);
        if (cartHeader != null) {
            cartHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AboutUs.this, BuyInCart.class));
                }
            });
        }
    }
}
