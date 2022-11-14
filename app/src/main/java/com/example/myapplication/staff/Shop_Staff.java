package com.example.myapplication.staff;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.adapter.ShoesAdapter;
import com.example.models.Shoes;
import com.example.myapplication.R;
import com.example.myapplication.customer.CreateProfileActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Shop_Staff extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GridView lvshoes;
    Shoes ashoe=null;
    String imglink;
    FloatingActionButton fabAddShoe;
    ActivityResultLauncher<Intent> upLoadImg= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null) {
                        Uri imageUri = result.getData().getData();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_staff);
        lvshoes=findViewById(R.id.listshoe);
        fabAddShoe=findViewById(R.id.fabAddShoe);
        fabAddShoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddShoesDialog(null);
            }
        });
        loadData();

        lvshoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData(){
        db.collection("Shoe")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Shoes> list = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                String id = document.getId();
                                String brand = map.get("Brand").toString();
                                String name = map.get("Name").toString();
                                Long price = (Long) map.get("Price");
                                String image = map.get("Image").toString();
                                String details = map.get("Details").toString();
                                Shoes shoe =new Shoes(id,name,brand,price,image,details);
                                list.add(shoe);
                            }

                            ShoesAdapter adapter = new ShoesAdapter(list);
                            lvshoes.setAdapter(adapter);
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void AddShoesDialog(Shoes shoe) {
        EditText editBrand, editName, editPrice, editDetails;
        CircleImageView editImage;
        LayoutInflater mLayoutInflater = getLayoutInflater();
        AlertDialog alertDialog;
        Shoes ashoe=shoe;
        View view = mLayoutInflater.inflate(R.layout.dialog_addshoe, null);
        editName=view.findViewById(R.id.txtShoeName);
        editBrand=view.findViewById(R.id.txtShoeBrand);
        editPrice=view.findViewById(R.id.txtShoePrice);
        editDetails=view.findViewById(R.id.txtShoeDetails);
        editImage=view.findViewById(R.id.ShoePic);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
                    upLoadImg.launch(pickIntent);
                Glide.with(Shop_Staff.this)
                        .load(imglink)
                        .into(editImage);
            }
        });

        


        if (ashoe==null){
            AlertDialog.Builder builderadd = new AlertDialog.Builder(Shop_Staff.this)
                    .setView(view)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String Name = editName.getText().toString();
                            String Brand = editBrand.getText().toString();
                            Long Price = Long.valueOf(editPrice.getText().toString()) ;
                            String Details = editDetails.getText().toString();
                            Map<String, Object> item = new HashMap<>();
                            item.put("Name", Name);
                            item.put("Brand", Brand);
                            item.put("Price", Price);
                            item.put("Details", Details);
                            item.put("Image",imglink);
                            db.collection("Shoe")
                                    .add(item)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(Shop_Staff.this, "Shoe added", Toast.LENGTH_SHORT).show();
                                            loadData();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Shop_Staff.this, "Failure, skill issue", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
            alertDialog = builderadd.create();
        }


        else {
            AlertDialog.Builder builderedit = new AlertDialog.Builder(Shop_Staff.this)
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
            alertDialog = builderedit.create();
        }
        alertDialog.show();
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

    void useImage(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        upLoadImage(bitmap);
    }
}