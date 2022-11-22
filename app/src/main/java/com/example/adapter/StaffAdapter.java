package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.models.AppUsers;
import com.example.models.Shoes;
import com.example.myapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffAdapter extends BaseAdapter {
    public ArrayList<AppUsers> staffappuser;

    public StaffAdapter(ArrayList<AppUsers> staffappuser) {
        this.staffappuser = staffappuser;
    }

    @Override
    public int getCount() {
        return this.staffappuser.size();
    }

    @Override
    public Object getItem(int position) {
        return this.staffappuser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View _view, ViewGroup _viewGroup) {
        View view = _view;
        ImageView imgStaff = null;
        if (view == null) {
            view = View.inflate(_viewGroup.getContext(), R.layout.members_item, null);
            TextView profileRole = view.findViewById(R.id.profileRole);
            TextView profileName = view.findViewById(R.id.profileName);
            imgStaff = (ImageView) view.findViewById(R.id.profilePicture);
            StaffAdapter.ViewHolder holder = new StaffAdapter.ViewHolder(profileName,profileRole, imgStaff);
            view.setTag(holder);
        }
        Shoes shoe = (Shoes) getItem(i);
        StaffAdapter.ViewHolder holder = (StaffAdapter.ViewHolder) view.getTag();
        holder.profileRole.setText(shoe.getName());
        holder.profileName.setText("" + shoe.getPrice());
        Glide.with(view.getContext()).load(shoe.getImage()).into(holder.imgStaff);
        return view;
    }



        private static class ViewHolder{
            final TextView profileRole, profileName;
            final ImageView imgStaff;

            public ViewHolder(TextView profileRole, TextView profileName, ImageView imgStaff) {
                this.profileRole = profileRole;
                this.profileName = profileName;
                this.imgStaff = imgStaff;
            }
        }

    }


