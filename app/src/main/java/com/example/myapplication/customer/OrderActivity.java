package com.example.myapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.adapter.OrderAdapter;
import com.example.adapter.ShoesAdapter;
import com.example.models.Orders;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

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
}