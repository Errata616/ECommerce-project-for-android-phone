package com.example.bt1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    EditText username;
    EditText passwd;
    Button loginButton;
    Button forgetButton;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        passwd = findViewById(R.id.passwd);
        loginButton = findViewById(R.id.loginButton);
        forgetButton = findViewById(R.id.forgetButton);
        signUpButton = findViewById(R.id.signUpButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUser = username.getText().toString().trim();
                String inputPass = passwd.getText().toString().trim();

                SharedPreferences savedUserPreference = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String savedUser = savedUserPreference.getString("registeredUsername", "Duy");
                String savedPass = savedUserPreference.getString("registeredPassword", "Duy123");

                if (inputUser.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập tên tài khoản", Toast.LENGTH_SHORT).show();
                }

                else if (!inputUser.equals(savedUser)) {
                    Toast.makeText(Login.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                }

                else if (!inputPass.equals(savedPass)) {
                    Toast.makeText(Login.this, "Mật khẩu sai! Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent listRedirect = new Intent(Login.this, MainActivity.class);
                    startActivity(listRedirect);
                    finish();
                }
            }
        });

        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(Login.this, ResetPassword.class);
                startActivity(reset);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this, SignUp.class);
                startActivity(register);
            }
        });
    }
}
