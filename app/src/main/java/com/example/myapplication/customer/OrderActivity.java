package com.example.myapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.adapter.DetailAdapter;
import com.example.adapter.OrderAdapter;
import com.example.models.Orders;
import com.example.models.OrdersDetails;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {
    String id;
    ListView orderlist;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderlist=findViewById(R.id.listorder);
        Bundle extras = getIntent().getExtras();
        id = extras.getString("IdUser");
        orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Orders order = (Orders) parent.getItemAtPosition(position);
                loadOrderDetails(order);
            }
        });
    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

    //getting orders with matching user id
    public void loadData(){
        db.collection("Orders")
                .whereEqualTo("userid","/AppUsers/"+id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Orders> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String id= document.getId();
                                Timestamp time = (Timestamp) map.get("date");
                                String date = time.toDate().toString();
                                date = date.replace("GMT+07:00","");
                                String status = map.get("status").toString();
                                Long total = (Long) map.get("total");
                                Orders order =new Orders(id,total,date,status);
                                list.add(order);
                            }
                            OrderAdapter adapter = new OrderAdapter(list);
                            orderlist.setAdapter(adapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //getting orders details with matching order id
    public void loadOrderDetails(Orders order){
        db.collection("OrdersDetails")
                .whereEqualTo("orderid","/Orders/"+order.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<OrdersDetails> detailslist = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                Long quantity = (Long) map.get("quantity");
                                String shoeid = map.get("shoeid").toString();
                                String orderid = map.get("orderid").toString();
                                String name = map.get("name").toString();
                                name  = name.replace("/Shoe/","");
                                String price = map.get("price").toString();
                                price  = price.replace("/Shoe/","");
                                String size = map.get("size").toString();
                                size  = size.replace("/Shoe/","");
                                String color = map.get("color").toString();
                                color  = color.replace("/Shoe/","");
                                String image = map.get("image").toString();
                                image  = image.replace("/Shoe/","");
                                OrdersDetails details = new OrdersDetails(orderid,shoeid,image,name,price,color,size,quantity);
                                detailslist.add(details);
                                setDetailslist(detailslist);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //dialog showing bought items
    public void setDetailslist(ArrayList<OrdersDetails> list){
        AlertDialog alertDialog;
        LayoutInflater mLayoutInflater = getLayoutInflater();
        View view = mLayoutInflater.inflate(R.layout.dialog_ordersdetails, null);
        ListView lv =view.findViewById(R.id.listdetails);
        DetailAdapter adapter = new DetailAdapter(list);
        lv.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this)
                .setView(view);
        alertDialog = builder.create();
        alertDialog.show();
    }
}