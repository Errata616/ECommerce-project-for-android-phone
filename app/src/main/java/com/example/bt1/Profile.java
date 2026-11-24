package com.example.bt1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {
    Button backProfile;
    Button cartHeader;
    TextView profileUsername;
    TextView fullName;
    TextView email;
    TextView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_profile);

        backProfile = findViewById(R.id.backProfile);
        cartHeader = findViewById(R.id.cartHeader);
        profileUsername = findViewById(R.id.profileUsername);
        fullName = findViewById(R.id.FullName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = pref.getString("registeredUsername", "Chưa cập nhật");
        String mail = pref.getString("registeredEmail", "Chưa cập nhật");
        String phoneNumber = pref.getString("registeredPhone", "Chưa cập nhật");

        if (profileUsername != null) profileUsername.setText(username);
        if (fullName != null) fullName.setText(username);
        if (email != null) email.setText(mail);
        if (phone != null) phone.setText(phoneNumber);

        cartHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartRedirect = new Intent(Profile.this, BuyInCart.class);
                startActivity(cartRedirect);
            }
        });

        if(backProfile != null) {
            backProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
