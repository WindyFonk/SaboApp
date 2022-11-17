package com.example.myapplication.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView Name, Address, Phonenumber, Email;
    CircleImageView Pfppic;
    ImageView editAddress, editPhonenumber, editEmail,editPass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        loadInfo();
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("IdUser");

        Name=findViewById(R.id.pfname);
        Address=findViewById(R.id.pfaddess);
        Phonenumber=findViewById(R.id.pfphone);
        Email=findViewById(R.id.pfemail);
        Pfppic=findViewById(R.id.pfppic);
        editAddress = findViewById(R.id.editAddress);
        editEmail = findViewById(R.id.editEmail);
        editPhonenumber = findViewById(R.id.editPhone);
        editPass = findViewById(R.id.editPassword);

        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog("name");
            }
        });

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog("address");
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog("email");
            }
        });

        editPhonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog("phone");
            }
        });

        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialog("password");
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

    public void EditDialog(String info) {
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("IdUser");
        LayoutInflater mLayoutInflater = getLayoutInflater();
        AlertDialog alertDialog;
        View view = mLayoutInflater.inflate(R.layout.editdialog, null);
        EditText edtDialog = view.findViewById(R.id.edtImport);
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
                            String data = edtDialog.getText().toString();
                            if (info.equals("address")){
                                db.collection("AppUsers")
                                        .document(id).update("address",data);
                                loadInfo();
                            } else if (info.equals("email")){
                                db.collection("AppUsers")
                                        .document(id).update("email",data);
                                loadInfo();
                            } else if (info.equals("phone")){
                                db.collection("AppUsers")
                                        .document(id).update("phonenumber",data);
                                loadInfo();
                            }
                            else if (info.equals("password")){
                                db.collection("AppUsers")
                                        .document(id).update("password",data);
                                loadInfo();
                            }

                            else if (info.equals("name")){
                                db.collection("AppUsers")
                                        .document(id).update("name",data);
                                loadInfo();
                            }
                        }
                    });
            alertDialog = builderadd.create();
            alertDialog.show();
    }

}