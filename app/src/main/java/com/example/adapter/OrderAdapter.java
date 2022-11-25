package com.example.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.models.Orders;
import com.example.models.Shoes;
import com.example.myapplication.R;

import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {
    public ArrayList<Orders> list;
    public OrderAdapter(ArrayList<Orders> list){
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
        if (view == null) {
            view = View.inflate(_viewGroup.getContext(), R.layout.item_order, null);
            TextView tvDate = view.findViewById(R.id.orderDate);
            TextView tvTotal = view.findViewById(R.id.orderTotal);
            TextView tvStatus = view.findViewById(R.id.orderStatus);
            ViewHolder holder = new ViewHolder(tvDate, tvTotal,tvStatus);
            view.setTag(holder);
        }
        Orders order = (Orders) getItem(i);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tvDate.setText(order.getDate());
        holder.tvTotal.setText("" + order.getTotal());
        holder.tvStatus.setText(order.getStatus());

        return view;
    }

    static class ViewHolder{
        final TextView tvDate, tvTotal,tvStatus;

        public ViewHolder(TextView tvDate, TextView tvTotal, TextView tvStatus) {
            this.tvStatus = tvStatus;
            this.tvTotal = tvTotal;
            this.tvDate = tvDate;
        }
    }
}
