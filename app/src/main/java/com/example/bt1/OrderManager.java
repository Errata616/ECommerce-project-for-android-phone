package com.example.bt1;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static OrderManager instance;
    private List<OrderModel> orderList;
    private OrderManager() {
        orderList = new ArrayList<>();
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public List<OrderModel> getOrderList() {
        return orderList;
    }
    public void addOrder(OrderModel order) {
        orderList.add(0, order);
    }
}
