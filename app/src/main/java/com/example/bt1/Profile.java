package com.example.bt1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {
    Button cartHeader;
    TextView profileUsername;
    TextView fullName;
    TextView email;
    TextView phone;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_profile);

        cartHeader = findViewById(R.id.cartHeader);
        profileUsername = findViewById(R.id.profileUsername);
        fullName = findViewById(R.id.FullName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_account);

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = pref.getString("registeredUsername", "Chưa cập nhật");
        String mail = pref.getString("registeredEmail", "Chưa cập nhật");
        String phoneNumber = pref.getString("registeredPhone", "Chưa cập nhật");

        if (profileUsername != null) profileUsername.setText(username);
        if (fullName != null) fullName.setText(username);
        if (email != null) email.setText(mail);
        if (phone != null) phone.setText(phoneNumber);

        if (cartHeader != null) {
            cartHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cartRedirect = new Intent(Profile.this, BuyInCart.class);
                    startActivity(cartRedirect);
                }
            });
        }

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(Profile.this, MainActivity.class));
                    finish();
                    return true;
                }

                else if (id == R.id.nav_orders) {
                    startActivity(new Intent(Profile.this, OrderActivity.class));
                    finish();
                    return true;
                }

                else if (id == R.id.nav_account) {
                    return true;
                }
                return false;
            }
        });
    }
}
