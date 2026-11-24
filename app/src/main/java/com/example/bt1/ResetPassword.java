package com.example.bt1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPassword extends AppCompatActivity {
    EditText newPassword;
    EditText confirmNewPassword;
    Button submitResetPass;
    Button backResetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_reset_password);

        backResetPass = findViewById(R.id.backResetPass);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        submitResetPass = findViewById(R.id.submitResetPass);

        if (backResetPass != null) {
            backResetPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (submitResetPass != null) {
            submitResetPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newPass = newPassword != null ? newPassword.getText().toString().trim() : "";
                    String confirmPass = confirmNewPassword != null ? confirmNewPassword.getText().toString().trim() : "";

                    if (newPass.isEmpty()) {
                        Toast.makeText(ResetPassword.this, "Vui lòng nhập mật khẩu mới!", Toast.LENGTH_SHORT).show();
                    }

                    else if (!newPass.equals(confirmPass)) {
                        Toast.makeText(ResetPassword.this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        SharedPreferences pref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        pref.edit().putString("registeredPassword", newPass).apply();

                        Toast.makeText(ResetPassword.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }
}
