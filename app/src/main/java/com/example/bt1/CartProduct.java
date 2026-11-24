package com.example.bt1;

public class CartProduct {
    private String productName;
    private int productPrice;
    private int productQuantity;
    private int productImage;

    public CartProduct(String productName, int productPrice, int productQuantity, int productImage) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }
    public int getProductPrice() {
        return productPrice;
    }
    public int getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
    public int getProductImage() {
        return productImage;
    }
    public int getTotalPrice() {
        return productPrice * productQuantity;
    }
}
