package com.example.myapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.adapter.AdapterCart;
import com.example.adapter.ShoesAdapter;
import com.example.models.Cart;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView lvcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        lvcart = findViewById(R.id.listcart);
        loadData();

        lvcart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData(){
        db.collection("Cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Cart> list = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String brand = map.get("Brand").toString();
                                String name = map.get("Name").toString();
                                Long price = (Long) map.get("Price");
                                String image = map.get("Image").toString();
                         //       Cart cart =new Cart();
                          //      list.add(cart);
                            }
                          //  AdapterCart adapter = new AdapterCart();
                          //  lvcart.setAdapter(adapter);
                      //  } else {
                       //     Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}