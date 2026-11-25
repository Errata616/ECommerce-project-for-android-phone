package com.example.bt1;

import java.io.Serializable;

public class OrderModel implements Serializable {
    private String orderId;
    private String customerName;
    private String phone;
    private String address;
    private int totalPrice;
    private String orderDate;

    public OrderModel(String orderId, String customerName, String phone, String address, int totalPrice, String orderDate) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getPhone() {
        return phone;
    }
    public String getAddress() {
        return address;
    }
    public int getTotalPrice() {
        return totalPrice;
    }
    public String getOrderDate() {
        return orderDate;
    }
}
