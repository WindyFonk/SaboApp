package com.example.myapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.CartAdapter;
import com.example.library.TinyDB;
import com.example.models.Orders;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    ArrayList<Object> cartlistobj = new ArrayList<Object>();
    ArrayList<Shoes> shoplist = new ArrayList<>();
    ListView lvcart;
    TinyDB tinydb;
    Button btnPurchase;
    TextView total;
    String id;
    Date date = Calendar.getInstance().getTime();
    Long totalprice;
    ArrayList<Object> orderlist;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        lvcart = findViewById(R.id.listcart);
        total=findViewById(R.id.txtTotal);
        btnPurchase = findViewById(R.id.btnPur);
        tinydb = new TinyDB(CartActivity.this);
        cartlistobj=tinydb.getListObject("CartList",Shoes.class);
        tinydb = new TinyDB(CartActivity.this);
        orderlist=tinydb.getListObject("OrderList", Orders.class);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("IdUser");
        
        //delete items
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
                                loadData();
                            }})
                        .setNegativeButton("Cancel", null).show();
                return true;
            }
        });
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> item = new HashMap<>();
                item.put("date", date);
                item.put("status", "Delivery");
                item.put("total", totalprice);
                item.put("userid", "/AppUsers/"+id);
                db.collection("Orders")
                        .add(item)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(CartActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData(){
        totalprice = Long.valueOf(0);
        for (int i=0;i<cartlistobj.size();i++){
            Shoes shoe = (Shoes) cartlistobj.get(i);
            shoplist.add(shoe);
            totalprice+=shoe.getPrice();
            Log.d(">>>>CARTTAG",shoplist.get(i).toString());
        }
        CartAdapter adapterCart = new CartAdapter(shoplist);
        Log.d(">>>TAGSIZE: ",""+shoplist.size());
        if (shoplist.size()==0){
            btnPurchase.setEnabled(false);
        }
        else{
            btnPurchase.setEnabled(true);
        }
        lvcart.setAdapter(adapterCart);
        total.setText("Total: " + totalprice);
    }
}