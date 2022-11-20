package com.example.myapplication.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.hardware.lights.LightState;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.models.Shoes;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.annotations.Until;

import java.text.DecimalFormat;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    TextView txtTotal;
    ImageView imgV;
    ListView listView;
    Button btnPur;
    List<Shoes> shoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        imgV = findViewById(R.id.imageView);
        listView=findViewById(R.id.listorder);
        txtTotal = findViewById(R.id.txtTotal);
        btnPur = findViewById(R.id.btnPur);

        DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        long total = getIntent().getLongExtra("total",0);
        txtTotal.setText(decimalFormat.format(total));

        btnPur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}