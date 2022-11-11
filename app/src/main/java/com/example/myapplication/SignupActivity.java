package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText txemail, txpassword, txrepass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        txemail=findViewById(R.id.txtEmailsu);
        txpassword=findViewById(R.id.txtPasswordsu);
        txrepass=findViewById(R.id.txtRePasswordsu);
        btnSignup=findViewById(R.id.btnSignupclick);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txemail.getText().toString();
                String password = txpassword.getText().toString();
                String repass = txrepass.getText().toString();
                validCheck(email,password,repass);

                if (validCheck(email,password,repass)==true){
                    onSignupClick();
                }
            }
        });
    }

    private boolean validCheck(String email, String password, String repass){
        if (email.length()==0){
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (password.length()==0){
            Toast.makeText(this, "Vui lòng nhập password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (repass.length()==0){
            Toast.makeText(this, "Vui lòng nhập lại password", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (repass.equals(password)==false){
            Toast.makeText(this, "Password nhập lại không đúng", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    public void onSignupClick(){
        //add new user
        String email = txemail.getText().toString();
        String password = txpassword.getText().toString();

        Map<String, Object> item = new HashMap<>();
        item.put("email", email);
        item.put("password", password);
        item.put("phonenumber", "");
        item.put("address", "");
        item.put("image","");
        db.collection("AppUsers")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        //get and set user ID for further uses
                        db.collection("AppUsers")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String id = document.getId();
                                                SharedPreferences sharedPreferences =
                                                        getSharedPreferences("USER",MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("Id",id);
                                                editor.putString("email",email);
                                                editor.putString("password",password);
                                                editor.commit();
                                            }
                                        } else {
                                            Log.w("TAG", "Error getting documents.", task.getException());
                                        }
                                    }
                                });
                        Intent intent = new Intent(SignupActivity.this,CreateProfileActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}