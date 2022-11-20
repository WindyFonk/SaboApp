package com.example.myapplication.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.CartAdapter;
import com.example.library.TinyDB;
import com.example.models.Orders;
import com.example.models.Shoes;
import com.example.myapplication.R;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ArrayList<Object> cartlistobj = new ArrayList<Object>();
    ArrayList<Shoes> shoplist = new ArrayList<>();
    ListView lvcart;
    TinyDB tinydb;
    ArrayList<Object> orderlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        lvcart = findViewById(R.id.listcart);
        tinydb = new TinyDB(CartActivity.this);
        cartlistobj=tinydb.getListObject("CartList",Shoes.class);
        tinydb = new TinyDB(CartActivity.this);
        orderlist=tinydb.getListObject("OrderList", Orders.class);

        lvcart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Confirm delete")
                        .setMessage("Do you want to remove this item?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                shoplist.remove(position);
                                cartlistobj.remove(position);
                                tinydb.putListObject("CartList",cartlistobj);
                                Toast.makeText(CartActivity.this, "Removed",
                                        Toast.LENGTH_LONG).show();
                                CartAdapter adapterCart = new CartAdapter(shoplist);
                                lvcart.setAdapter(adapterCart);
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
        for (int i=0;i<cartlistobj.size();i++){
            Shoes shoe = (Shoes) cartlistobj.get(i);
            shoplist.add(shoe);
            Log.d(">>>>CARTTAG",shoplist.get(i).toString());
        }
        CartAdapter adapterCart = new CartAdapter(shoplist);
        lvcart.setAdapter(adapterCart);
    }
}