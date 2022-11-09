package com.example.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.models.Shoes;
import com.example.myapplication.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShoesAdapter extends BaseAdapter {
    public ArrayList<Shoes> list;
    public ShoesAdapter(ArrayList<Shoes> list){
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
        CircleImageView imgShoe = null;
        if (view == null) {
            view = View.inflate(_viewGroup.getContext(), R.layout.shoe_items, null);
            TextView tvShoename = view.findViewById(R.id.shoename);
            TextView tvShoeprice = view.findViewById(R.id.shoeprice);
            imgShoe = (CircleImageView) view.findViewById(R.id.shoeimg);
            ViewHolder holder = new ViewHolder(tvShoename, tvShoeprice, imgShoe);
            view.setTag(holder);
        }
        Shoes shoe = (Shoes) getItem(i);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tvShoename.setText(shoe.getName());
        holder.tvShoeprice.setText("" + shoe.getPrice());
        Uri downloadUri = Uri.parse(shoe.getImage());
        Glide.with(view.getContext()).load(downloadUri).into(imgShoe);
        return view;
    }

    private static class ViewHolder{
        final TextView tvShoename, tvShoeprice;
        final CircleImageView imgShoe;

        public ViewHolder(TextView tvShoename, TextView tvShoeprice, CircleImageView imgShoe) {
            this.tvShoename = tvShoename;
            this.tvShoeprice = tvShoeprice;
            this.imgShoe = imgShoe;
        }
    }

}
