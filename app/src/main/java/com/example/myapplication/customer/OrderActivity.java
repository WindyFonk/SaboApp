package com.example.myapplication.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.lights.LightState;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.adapter.CartAdapter;
import com.example.adapter.OrderAdapter;
import com.example.library.TinyDB;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.annotations.Until;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    ArrayList<Object> orderlistobj = new ArrayList<Object>();
    ArrayList<Shoes> cartlist = new ArrayList<>();
    TextView txtTotal;
    ImageView imgV;
    ListView listView;
    Button btnPur;
    TinyDB tinydb;

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
                Toast.makeText(OrderActivity.this,"Purchase Successful",Toast.LENGTH_SHORT);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(OrderActivity.this)
                        .setTitle("Confirm delete")
                        .setMessage("Do you want to remove this item?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                cartlist.remove(position);
                                orderlistobj.remove(position);
                                tinydb.putListObject("OrderList",orderlistobj);
                                Toast.makeText(OrderActivity.this, "Removed",
                                        Toast.LENGTH_LONG).show();
                                OrderAdapter adapterOrder = new OrderAdapter(cartlist);
                                listView.setAdapter(adapterOrder);
                            }})
                        .setNegativeButton("Cancel", null).show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData(){
        for (int i=0;i<orderlistobj.size();i++){
            Shoes shoe = (Shoes) orderlistobj.get(i);
            cartlist.add(shoe);
            Log.d(">>>>ORDERTAG",cartlist.get(i).toString());
        }
        OrderAdapter adapterOrder = new OrderAdapter(cartlist);
        listView.setAdapter(adapterOrder);
    }
}