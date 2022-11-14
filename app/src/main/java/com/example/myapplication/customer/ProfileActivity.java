package com.example.myapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView Name, Address, Phonenumber, Email;
    CircleImageView Pfppic;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Name=findViewById(R.id.pfname);
        Address=findViewById(R.id.pfaddess);
        Phonenumber=findViewById(R.id.pfphone);
        Email=findViewById(R.id.pfemail);
        Pfppic=findViewById(R.id.pfppic);
        loadInfo();
    }

    private void loadInfo(){
        SharedPreferences sharedPreferences =
                getSharedPreferences("USER",MODE_PRIVATE);
        String id = sharedPreferences.getString("Id",null);

        DocumentReference docRef = db.collection("AppUsers").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //get and set user's info
                        String name = (String) document.get("name");
                        String address = (String) document.get("address");
                        String image = (String) document.get("image");
                        String phonenumber = (String) document.get("phonenumber");
                        String email= (String) document.get("email");
                        Name.setText(name);
                        Address.setText(address);
                        Phonenumber.setText(phonenumber);
                        Email.setText(email);
                        Glide.with(ProfileActivity.this)
                                .load(image)
                                .into(Pfppic);
                        Log.d(">>TAG", name +"\n"+address+"\n"+image);
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