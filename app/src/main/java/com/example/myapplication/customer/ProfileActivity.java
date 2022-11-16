package com.example.myapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
    ImageView imvDialog;

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
        imvDialog = findViewById(R.id.imvDialog);
        loadInfo();




        imvDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog();

            }
        });
    }

    private void loadInfo(){
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("IdUser");
        Log.d(">>>USER ID:",id);

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

    public void EditDialog() {

        EditText edtDialog = findViewById(R.id.edtDialog);
        EditText edtQuestion = findViewById(R.id.edtDialog);
        EditText edtImport = findViewById(R.id.edtImport);

        LayoutInflater mLayoutInflater = getLayoutInflater();
        AlertDialog alertDialog;
        View view = mLayoutInflater.inflate(R.layout.editdialog, null);
            AlertDialog.Builder builderadd = new AlertDialog.Builder(ProfileActivity.this)
                    .setView(view)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            alertDialog = builderadd.create();

            alertDialog.show();
    }

}