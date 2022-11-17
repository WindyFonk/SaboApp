package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.models.Cart;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.example.myapplication.customer.CartActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
            imgShoe = view.findViewById(R.id.img);
            ViewHolder holder = new ViewHolder(tvShoename, tvShoeprice,tvShoesize, imgShoe);
            view.setTag(holder);
        }
        Shoes shoe = (Shoes) getItem(i);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tvShoename.setText(shoe.getName());
        holder.tvShoeprice.setText("" + shoe.getPrice());
        Glide.with(view.getContext()).load(shoe.getImage()).into(holder.imgShoe);
        return view;
    }

    private static class ViewHolder{
        final TextView tvShoename, tvShoeprice,tvShoesize;
        final ImageView imgShoe;

        public ViewHolder(TextView tvShoename, TextView tvShoeprice, TextView tvShoesize, ImageView imgShoe) {
            this.tvShoename = tvShoename;
            this.tvShoeprice = tvShoeprice;
            this.imgShoe = imgShoe;
            this.tvShoesize=tvShoesize;
        }
    }
}