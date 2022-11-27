package com.example.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.models.OrdersDetails;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.example.myapplication.customer.CartActivity;

import java.util.ArrayList;

public class DetailAdapter extends BaseAdapter {
    public ArrayList<OrdersDetails> list;
    public DetailAdapter(ArrayList<OrdersDetails> list){
        this.list=list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int i, View _view, ViewGroup _viewGroup) {
        View view = _view;
        ImageView imgShoe = null;
        if (view == null) {
            view = View.inflate(_viewGroup.getContext(), R.layout.order_details_item, null);
            TextView tvShoename = view.findViewById(R.id.name);
            TextView tvShoeprice = view.findViewById(R.id.price);
            TextView tvShoesize = view.findViewById(R.id.shoesize);
            TextView tvShoecolor = view.findViewById(R.id.shoecolor);
            TextView tvShoequantity = view.findViewById(R.id.quantity);
            imgShoe = view.findViewById(R.id.img);
            ViewHolder holder = new ViewHolder(tvShoename, tvShoeprice, tvShoesize, tvShoecolor, imgShoe,tvShoequantity);
            view.setTag(holder);
        }
        OrdersDetails shoe = (OrdersDetails) getItem(i);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tvShoename.setText(shoe.getName());
        holder.tvShoeprice.setText("" + shoe.getPrice());
        holder.tvShoesize.setText("Size: " + shoe.getSize());
        holder.tvShoecolor.setText("Color: " + shoe.getColor());
        holder.tvShoequantity.setText(""+shoe.getQuantity());
        Glide.with(view.getContext()).load(shoe.getImage()).into(holder.imgShoe);
        return view;
    }

    private static class ViewHolder{
        final TextView tvShoename, tvShoeprice,tvShoesize,tvShoecolor,tvShoequantity;
        final ImageView imgShoe;


        public ViewHolder(TextView tvShoename, TextView tvShoeprice, TextView tvShoesize,
                          TextView tvShoecolor, ImageView imgShoe, TextView tvShoequantity) {
            this.tvShoename = tvShoename;
            this.tvShoeprice = tvShoeprice;
            this.imgShoe = imgShoe;
            this.tvShoesize=tvShoesize;
            this.tvShoecolor = tvShoecolor;
            this.tvShoequantity = tvShoequantity;
        }
    }
}