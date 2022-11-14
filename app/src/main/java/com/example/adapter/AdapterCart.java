package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.models.Cart;
import com.example.myapplication.R;

import java.util.Calendar;
import java.util.List;

public class AdapterCart extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Cart> arrList;

    public AdapterCart(Context context, int layout, List<Cart> arrList) {
        this.context = context;
        this.layout = layout;
        this.arrList = arrList;
    }

    @Override
    public int getCount() {
        return arrList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout,null);

        Cart cart = arrList.get(i);

        TextView tvTen = view.findViewById(R.id.name);
        TextView tvPrice = view.findViewById(R.id.price);
        TextView tvSize = view.findViewById(R.id.shoesize);
        ImageView img = view.findViewById(R.id.img);

        tvTen.setText(cart.getName());
        tvPrice.setText(cart.getPrice());
        tvSize.setText(cart.getSize());
        img.setImageResource(cart.getImg());
        return view;
    }
}