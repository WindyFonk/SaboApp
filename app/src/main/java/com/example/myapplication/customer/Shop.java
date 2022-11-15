package com.example.myapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.adapter.ShoesAdapter;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Shop extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GridView lvshoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        lvshoes=findViewById(R.id.listshoe);
        loadData();

        lvshoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long _id) {
                Shoes shoe= (Shoes) parent.getItemAtPosition(position);
                String id = shoe.getId();
                Intent intent = new Intent(Shop.this, ItemActivity.class);
                intent.putExtra("idshoe",id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData(){
        db.collection("Shoe")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Shoes> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String id= document.getId();
                                String brand = map.get("Brand").toString();
                                String name = map.get("Name").toString();
                                Long price = (Long) map.get("Price");
                                String image = map.get("Image").toString();
                                String details = map.get("Details").toString();
                                String size= (String) map.get("Size");
                                String color = (String) map.get("Color");
                                Shoes shoe =new Shoes(id,name,brand,price,image,details,size,color);
                                list.add(shoe);
                            }
                            ShoesAdapter adapter = new ShoesAdapter(list);
                            lvshoes.setAdapter(adapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}