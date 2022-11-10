package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.models.AppUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText txEmail, txPassword;
    Button btnLogin, btnSignup;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txEmail=findViewById(R.id.txtEmail);
        txPassword=findViewById(R.id.txtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnSignup=findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =txEmail.getText().toString();
                String password =txPassword.getText().toString();
                validCheck(email,password);
                if (validCheck(email,password)==true){
                    onLoginClick();
                }
            }
        });
    }

    private boolean validCheck(String email, String password){
        if (email.length()==0){
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (password.length()==0){
            Toast.makeText(this, "Vui lòng nhập password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    public void onLoginClick(){
        String _email=txEmail.getText().toString();
        String _password=txPassword.getText().toString();

        db.collection("AppUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String email = map.get("email").toString();
                                String password = map.get("password").toString();
                                if (_email.equals(email) && _password.equals(password)){
                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}