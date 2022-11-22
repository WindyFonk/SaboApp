package com.example.myapplication.admin;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adapter.StaffAdapter;
import com.example.models.AppUsers;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.example.myapplication.staff.Shop_Staff;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ManagerActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword, edtPhone, edtRole, edtOldName, edtNewName;
    FirebaseFirestore db;


    ProgressDialog progressDialog;
    ListView ListMembers;
    String imgStaff;
    AppUsers appUsers = null;
    Button btnAddStaff;
    ImageView editStaffimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        //hiển thị tiến trình loaddata
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching Data.......");
//        progressDialog.show();

        //đưa data lên listview
        ListMembers = findViewById(R.id.ListMembers);


        db = FirebaseFirestore.getInstance();

        btnAddStaff = findViewById(R.id.btnAddMembers);
//        rtlEditStaff = findViewById(R.id.rtlEditStaff);


        ListMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListMembers = (ListView) parent.getItemAtPosition(position);
            }
        });


//        rtlEditStaff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditStaff();
//            }
//        });
        btnAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStaff();
            }
        });

    }

    private void StaffListner() {

    }

    //hàm lấy data

    public void EditStaff() {
        LayoutInflater mLayoutInflater = getLayoutInflater();
        AlertDialog alertDialog;
        View view = mLayoutInflater.inflate(R.layout.add_staff, null);
        AlertDialog.Builder builderadd = new AlertDialog.Builder(ManagerActivity.this)
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

    public void AddStaff() {
        LayoutInflater mLayoutInflater = getLayoutInflater();
        AlertDialog alertDialog;
        //khai báo
        View view = mLayoutInflater.inflate(R.layout.add_staff, null);
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtRole = view.findViewById(R.id.edtRole);
        AlertDialog.Builder builderadd = new AlertDialog.Builder(ManagerActivity.this)
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Tương tác với dialog đưa data lên fibse
                        String Name = edtName.getText().toString();
                        String Email = edtEmail.getText().toString();
                        String Phone = edtPhone.getText().toString();
                        String Role = edtRole.getText().toString();
                        Map<String, Object> AppUsers = new HashMap<>();

                        AppUsers.put("Name", Name);
                        AppUsers.put("Email", Email);
                        AppUsers.put("Phone", Phone);
                        AppUsers.put("Role", Role);

                        db.collection("AppUsers")
                                .add(AppUsers)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(ManagerActivity.this, "Add Staff Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ManagerActivity.this, "Add Staff Failec", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
        alertDialog = builderadd.create();
        alertDialog.show();

    }
}