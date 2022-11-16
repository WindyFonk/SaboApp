package com.example.myapplication.customer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity {

    EditText txAddress, txPhone, txName;
    CircleImageView pfpPic;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String imglink;
    Button gotoshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        txAddress = findViewById(R.id.txtAddress);
        txPhone=findViewById(R.id.txtPhone);
        gotoshop=findViewById(R.id.goShop);
        txName=findViewById(R.id.txtName);
        pfpPic=findViewById(R.id.createPfpPic);

        gotoshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateProfile();
            }
        });

        //Upload profile pics
        pfpPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean allowed = ActivityCompat
                        .checkSelfPermission(CreateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
                if (allowed) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
                    upLoadImg.launch(pickIntent);
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
                    upLoadImg.launch(pickIntent);
                } else {

                }
                break;
            }
            default:
                break;
        }
    }

    ActivityResultLauncher<Intent> upLoadImg= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null) {
                        Uri imageUri = result.getData().getData();
                        Glide.with(CreateProfileActivity.this)
                                .load(imageUri)
                                .into(pfpPic);
                        imglink=imageUri.toString();
                        try {
                            useImage(imageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    void useImage(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        upLoadImage(bitmap);
    }

    public void upLoadImage(Bitmap bitmap){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageReference = storageReference.child(Calendar.getInstance().getTimeInMillis()+".jpg");
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


    //adding user's information
    private void onCreateProfile(){
        SharedPreferences sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);
        String id= sharedPreferences.getString("Id",null);
        String email = sharedPreferences.getString("email",null);
        String password = sharedPreferences.getString("password",null);
        String phonenumb = txPhone.getText().toString();
        String address = txAddress.getText().toString();
        String name = txName.getText().toString();
        Map<String, Object> item = new HashMap<>();
        item.put("email", email);
        item.put("password", password);
        item.put("phonenumber", phonenumb);
        item.put("name", name);
        item.put("address", address);
        item.put("image",imglink);
        item.put("role",2);
        Log.d(">>>>TAG",""+id+"\n"+phonenumb+"\n"+address+"\n"+imglink);

        db.collection("AppUsers")
                .document(id)
                .set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateProfileActivity.this, "Chào mừng bạn đến với Sabo", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateProfileActivity.this,HomeActivity.class);
                        intent.putExtra("IdUser",id);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}
