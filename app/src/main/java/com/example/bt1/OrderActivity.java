package com.example.bt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    ListView ordersListView;
    BottomNavigationView bottomNavigationView;
    List<OrderModel> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_orders);

        ordersListView = findViewById(R.id.ordersListView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);

        orderList = OrderManager.getInstance().getOrderList();

        ArrayAdapter<OrderModel> adapter = new ArrayAdapter<OrderModel>(this, android.R.layout.simple_list_item_2, android.R.id.text1, orderList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                OrderModel order = getItem(position);
                if (order != null) {
                    TextView text1 = view.findViewById(android.R.id.text1);
                    TextView text2 = view.findViewById(android.R.id.text2);

                    text1.setText("Mã đơn: " + order.getOrderId() + " - Tổng: " + String.format("%,d đ", order.getTotalPrice()));
                    text2.setText("Người nhận: " + order.getCustomerName() + " | SĐT: " + order.getPhone() + "\nĐịa chỉ: " + order.getAddress() + "\nThời gian: " + order.getOrderDate());
                    text2.setTextColor(0xFF666666);
                }
                return view;
            }
        };

        ordersListView.setAdapter(adapter);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(OrderActivity.this, MainActivity.class));
                    finish();
                    return true;
                }

                else if (id == R.id.nav_orders) {
                    return true;
                }

                else if (id == R.id.nav_account) {
                    startActivity(new Intent(OrderActivity.this, Profile.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}
