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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adapter.ShoesAdapter;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
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

    EditText edtName, edtEmail;
    FirebaseFirestore db;
    ListView ListMembers;
    AppUsers appUsers = null;
    Button btnAddStaff;
    ImageView editStaffimg;
    Spinner edtRole;
    String imglink;

    ActivityResultLauncher<Intent> upLoadImg= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null) {
                        Uri imageUri = result.getData().getData();
                        imglink=imageUri.toString();
                        Glide.with(ManagerActivity.this)
                                .load(imglink)
                                .into(editStaffimg);
                        try {
                            useImage(imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ListMembers = findViewById(R.id.ListMembers);
        db = FirebaseFirestore.getInstance();
        btnAddStaff = findViewById(R.id.btnAddMembers);
        loadData();


        ListMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppUsers user = (AppUsers) parent.getItemAtPosition(position);
                AddStaff(user);
            }
        });

        btnAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStaff(null);
            }
        });
    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

    public void AddStaff(AppUsers user) {
        LayoutInflater mLayoutInflater = getLayoutInflater();
        AlertDialog alertDialog;
        appUsers = user;
        View view = mLayoutInflater.inflate(R.layout.add_staff, null);
        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);

        edtRole = (Spinner) view.findViewById(R.id.spinnerrole);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtRole.setAdapter(adapter);

        editStaffimg = view.findViewById(R.id.createPfpPic);

        editStaffimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                upLoadImg.launch(pickIntent);
            }
        });

        if (appUsers == null){
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
                            String Name = edtName.getText().toString();
                            String Email = edtEmail.getText().toString();
                            String Role = edtRole.getSelectedItem().toString();
                            int role;
                            Map<String, Object> AppUsers = new HashMap<>();
                            if (Role.equals("Admin")){
                                role = 0;
                            }
                            else{
                                role = 1;
                            }
                            Log.d(">>>TAG","Role: "+role);
                            AppUsers.put("name", Name);
                            AppUsers.put("email", Email);
                            AppUsers.put("phonenumber", "");
                            AppUsers.put("password", "");
                            AppUsers.put("image", imglink);
                            AppUsers.put("role", role);

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
        }


        else{
            imglink=appUsers.getImage();
            edtName.setText(appUsers.getName());
            edtEmail.setText(appUsers.getEmail());
            edtRole.setSelection(Math.toIntExact(appUsers.getRole()));
            Glide.with(ManagerActivity.this)
                    .load(imglink)
                    .into(editStaffimg);
            AlertDialog.Builder builderedit = new AlertDialog.Builder(ManagerActivity.this)
                    .setView(view)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String Name = edtName.getText().toString();
                            String Email = edtEmail.getText().toString();
                            String Role = edtRole.getSelectedItem().toString();
                            int role;
                            Map<String, Object> AppUsers = new HashMap<>();
                            if (Role.equals("Admin")){
                                role = 0;
                            }
                            else{
                                role = 1;
                            }
                            AppUsers.put("name", Name);
                            AppUsers.put("email", Email);
                            AppUsers.put("image", imglink);
                            AppUsers.put("role", role);

                            db.collection("AppUsers")
                                    .document(appUsers.getId())
                                    .update(AppUsers)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ManagerActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                            loadData();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                        }
                    });
            alertDialog = builderedit.create();
        }
        alertDialog.show();
    }


    public void upLoadImage(Bitmap bitmap){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageReference = storageReference.child(Calendar.getInstance().getTimeInMillis()+".png");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,stream);
        byte[] bytes = stream.toByteArray();
        UploadTask uploadTask = imageReference.putBytes(bytes);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (task.isSuccessful()) return  imageReference.getDownloadUrl();
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    imglink=downloadUri.toString();
                }
            }
        });
    }

    void useImage(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        upLoadImage(bitmap);
    }

    public void loadData() {
        db.collection("AppUsers")
                .whereLessThan("role", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<AppUsers> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String id = document.getId();
                                String name = map.get("name").toString();
                                String image = map.get("image").toString();
                                Long role = (Long) map.get("role");
                                String email = map.get("email").toString();
                                String password = map.get("password").toString();
                                String phonenumb = map.get("phonenumber").toString();
                                String address = map.get("address").toString();
                                AppUsers user = new AppUsers(id,email,password,name,address,phonenumb,role,image);
                                list.add(user);
                            }
                            StaffAdapter adapter = new StaffAdapter(list);
                            ListMembers.setAdapter(adapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}