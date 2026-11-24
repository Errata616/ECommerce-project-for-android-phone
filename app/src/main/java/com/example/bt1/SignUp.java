package com.example.bt1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {
    EditText nameSignUp;
    EditText mailSignUp;
    EditText phoneSignUp;
    EditText addressSignUp;
    EditText passSignUp;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_sign_up);

        nameSignUp = findViewById(R.id.nameSignUp);
        mailSignUp = findViewById(R.id.mailSignUp);
        phoneSignUp = findViewById(R.id.phoneSignUp);
        addressSignUp = findViewById(R.id.addressSignUp);
        passSignUp = findViewById(R.id.passSignUp);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameSignUp.getText().toString().trim();
                String mail = mailSignUp.getText().toString().trim();
                String phone = phoneSignUp.getText().toString().trim();
                String address = addressSignUp.getText().toString().trim();
                String pass = passSignUp.getText().toString().trim();

                if (name.isEmpty() ||  mail.isEmpty() || phone.isEmpty() || address.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(SignUp.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                }

                else {
                    SharedPreferences pref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("registeredUsername", name);
                    editor.putString("registeredEmail", mail);
                    editor.putString("registeredPhone", phone);
                    editor.putString("registeredAddress", address);
                    editor.putString("registeredPassword", pass);
                    editor.apply();

                    Toast.makeText(SignUp.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();


                    Intent login = new Intent(SignUp.this, Login.class);
                    startActivity(login);
                    finish();
                }
            }
        });
    }
}
