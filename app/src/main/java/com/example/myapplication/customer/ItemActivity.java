package com.example.myapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItemActivity extends AppCompatActivity {

    TextView Name, Color, Size, Details;
    ImageView Image;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoe_details);
        Name =findViewById(R.id.tvShoeName);
        Color =findViewById(R.id.tvShoeColor);
        Size =findViewById(R.id.tvShoeSize);
        Details =findViewById(R.id.tvShoeDetails);
        Image =findViewById(R.id.imgShoe);
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("idshoe");

        DocumentReference docRef = db.collection("Shoe").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //get and set user's info
                        String name = (String) document.get("Name");
                        String details = (String) document.get("Details");
                        String image = (String) document.get("Image");
                        String color = (String) document.get("Color");
                        String size = (String) document.get("Size");
                        Long price = (Long) document.get("Price");
                        String brand = (String) document.get("Brand");
                        Shoes shoe = new Shoes(id,name,brand,price,image,details,size,color);
                        Name.setText(name);
                        Details.setText(details);
                        Color.setText("Color: "+color);
                        Size.setText("Size: "+size);

                        Glide.with(ItemActivity.this)
                                .load(image)
                                .into(Image);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}