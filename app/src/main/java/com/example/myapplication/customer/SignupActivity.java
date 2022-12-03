package com.example.myapplication.customer;

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

import com.example.adapter.ShoesAdapter;
import com.example.models.Shoes;
import com.example.myapplication.R;
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

    private boolean identicalCheck(String email){
        //check identical emails
        db.collection("AppUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String _email = map.get("email").toString();
                                list.add(_email);
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
        return true;
    }

    public void onSignupClick(){
        //send data and launch CreateProfileActivity
        String email = txemail.getText().toString();
        String password = txpassword.getText().toString();
        Intent intent = new Intent(SignupActivity.this,CreateProfileActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        startActivity(intent);
        finish();
    }
}