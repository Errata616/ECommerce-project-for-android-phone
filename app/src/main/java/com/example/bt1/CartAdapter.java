package com.example.bt1;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class CartAdapter extends ArrayAdapter<CartProduct> {
    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private OnCartChangeListener listener;

    public CartAdapter(@NonNull Context context, @NonNull List<CartProduct> products, OnCartChangeListener listener) {
        super(context, 0, products);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cart, parent, false);
        }

        CartProduct item = getItem(position);

        ImageView img = convertView.findViewById(R.id.cartProductImage);
        TextView tvName = convertView.findViewById(R.id.cartProductName);
        TextView tvPrice = convertView.findViewById(R.id.cartProductPrice);
        TextView cartQuantity = convertView.findViewById(R.id.cartQuantity);
        Button cartDecrease = convertView.findViewById(R.id.cartDecrease);
        Button cartIncrease = convertView.findViewById(R.id.cartIncrease);

        if (item != null) {
            if (img != null) img.setImageResource(item.getProductImage());
            if (tvName != null) tvName.setText(item.getProductName());
            if (tvPrice != null) tvPrice.setText(String.format("%,d đ", item.getProductPrice()));
            if (cartQuantity != null) cartQuantity.setText(String.valueOf(item.getProductQuantity()));

            if (cartIncrease != null) {
                cartIncrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setProductQuantity(item.getProductQuantity() + 1);
                        if (cartQuantity != null) cartQuantity.setText(String.valueOf(item.getProductQuantity()));
                        if (listener != null) {
                            listener.onCartChanged();
                        }
                    }
                });
            }

            if (cartDecrease != null) {
                cartDecrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getProductQuantity() > 1) {
                            item.setProductQuantity(item.getProductQuantity() - 1);
                            if (cartQuantity != null) cartQuantity.setText(String.valueOf(item.getProductQuantity()));
                            if (listener != null) {
                                listener.onCartChanged();
                            }
                        }

                        else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Xác nhận xóa")
                                    .setMessage("Bạn có muốn loại mặt hàng '" + item.getProductName() + "' ra khỏi giỏ không?")
                                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            CartManager.getInstance().removeProductFromCart(item);
                                            remove(item);
                                            notifyDataSetChanged();
                                            if (listener != null) {
                                                listener.onCartChanged();
                                            }
                                        }
                                    })
                                    .setNegativeButton("Hủy", null)
                                    .show();
                        }
                    }
                });
            }
        }

        return convertView;
    }
}
