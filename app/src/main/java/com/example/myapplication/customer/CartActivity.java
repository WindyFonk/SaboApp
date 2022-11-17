package com.example.myapplication.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.adapter.CartAdapter;
import com.example.library.TinyDB;
import com.example.models.Shoes;
import com.example.myapplication.R;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ArrayList<Object> cartlistobj = new ArrayList<Object>();
    ArrayList<Shoes> shoplist = new ArrayList<>();
    ListView lvcart;
    TinyDB tinydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        lvcart = findViewById(R.id.listcart);
        tinydb = new TinyDB(CartActivity.this);
        loadData();
        cartlistobj=tinydb.getListObject("CartList",Shoes.class);
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