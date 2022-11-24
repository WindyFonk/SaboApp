package com.example.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.example.myapplication.customer.CartActivity;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    public ArrayList<Shoes> list;
    public CartAdapter(ArrayList<Shoes> list){
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
            view = View.inflate(_viewGroup.getContext(), R.layout.cart_items, null);
            TextView tvShoename = view.findViewById(R.id.name);
            TextView tvShoeprice = view.findViewById(R.id.price);
            TextView tvShoesize = view.findViewById(R.id.shoesize);
            TextView tvShoecolor = view.findViewById(R.id.shoecolor);
            TextView tvShoequantity = view.findViewById(R.id.quantity);
            ImageView minus = view.findViewById(R.id.minus);
            ImageView plus = view.findViewById(R.id.plus);
            imgShoe = view.findViewById(R.id.img);
            ViewHolder holder = new ViewHolder(tvShoename, tvShoeprice, tvShoesize, tvShoecolor, imgShoe,tvShoequantity,minus,plus);
            view.setTag(holder);
        }
        Shoes shoe = (Shoes) getItem(i);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tvShoename.setText(shoe.getName());
        holder.tvShoeprice.setText("" + shoe.getPrice());
        holder.tvShoesize.setText("Size: " + shoe.getSize());
        holder.tvShoecolor.setText("Color: " + shoe.getColor());
        holder.tvShoequantity.setText(""+shoe.getQuantity());
        Glide.with(view.getContext()).load(shoe.getImage()).into(holder.imgShoe);
        View finalView = view;
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shoe.getQuantity()==1){
                    shoe.setQuantity(1);
                }else{
                    shoe.setQuantity(shoe.getQuantity()-1);
                }
                Log.d(">>>TAG","-");
                Log.d(">>>TAGQUANTITY",""+shoe.getQuantity());
                holder.tvShoequantity.setText(""+shoe.getQuantity());
                Intent intent = new Intent("Get Total");
                intent.putExtra("Total",CartActivity.getTotalPrice(list));
                LocalBroadcastManager.getInstance(finalView.getContext()).sendBroadcast(intent);
                Log.d(">>>TAG TOTAL","-"+CartActivity.getTotalPrice(list));
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoe.setQuantity(shoe.getQuantity()+1);
                Log.d(">>>TAG","+");
                Log.d(">>>TAGQUANTITY",""+shoe.getQuantity());
                holder.tvShoequantity.setText(""+shoe.getQuantity());
                Intent intent = new Intent("Get Total");
                intent.putExtra("Total",CartActivity.getTotalPrice(list));
                LocalBroadcastManager.getInstance(finalView.getContext()).sendBroadcast(intent);
                Log.d(">>>TAG TOTAL","-"+CartActivity.getTotalPrice(list));
            }
        });
        return view;
    }

    private static class ViewHolder{
        final TextView tvShoename, tvShoeprice,tvShoesize,tvShoecolor,tvShoequantity;
        final ImageView imgShoe, minus, plus;


        public ViewHolder(TextView tvShoename, TextView tvShoeprice, TextView tvShoesize,
                          TextView tvShoecolor, ImageView imgShoe, TextView tvShoequantity,
                          ImageView minus, ImageView plus) {
            this.tvShoename = tvShoename;
            this.tvShoeprice = tvShoeprice;
            this.imgShoe = imgShoe;
            this.tvShoesize=tvShoesize;
            this.tvShoecolor = tvShoecolor;
            this.tvShoequantity = tvShoequantity;
            this.minus = minus;
            this.plus = plus;
        }
    }
}