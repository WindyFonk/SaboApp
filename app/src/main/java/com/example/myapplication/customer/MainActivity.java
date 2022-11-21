package com.example.myapplication.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                //Ánh xạ
        ivLogo = findViewById(R.id.ivLogo);

        //Sử dụng Glide with là với trang nào, load: dữ liệu nào, into: id của imageview
        Glide.with(this).load(R.mipmap.logo).into(ivLogo);

        //Sử dụng handler để tạo ra delay và dùng cách tạo activity nhanh để chuyển giao diện
         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {
                 startActivity(new Intent(MainActivity.this,LoginActivity.class));
                 finish();
             }
         },4500);

    }
}