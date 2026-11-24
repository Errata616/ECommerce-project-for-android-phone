package com.example.bt1;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartProduct> cartList;

    private CartManager() {
        cartList = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartProduct> getCartList() {
        return cartList;
    }

    public void addProductToCart(String name, int price, int imageResId) {
        for (CartProduct item : cartList) {
            if (item.getProductName().equals(name)) {
                item.setProductQuantity(item.getProductQuantity() + 1);
                return;
            }
        }
        cartList.add(new CartProduct(name, price, 1, imageResId));
    }

    public void removeProductFromCart(CartProduct item) {
        cartList.remove(item);
    }

    public void clearCart() {
        cartList.clear();
    }
}
