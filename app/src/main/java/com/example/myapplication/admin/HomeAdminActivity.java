package com.example.myapplication.admin;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adapter.FeaturedAdapter;
import com.example.adapter.FeaturedHelperClass;
import com.example.myapplication.R;
import com.example.myapplication.customer.CartActivity;
import com.example.myapplication.customer.LoginActivity;
import com.example.myapplication.customer.ProfileActivity;
import com.example.myapplication.customer.Shop;
import com.example.myapplication.staff.HomeActivity_Staff;
import com.example.myapplication.staff.OrderActivity_Staff;
import com.example.myapplication.staff.Shop_Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView featuredRecycler;
    RecyclerView.Adapter adapter;
    ImageView menuIcon,cartIcon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView Name, Address;
    CircleImageView Pfp;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        cartIcon=findViewById(R.id.cart);
        featuredRecycler = findViewById(R.id.featured_recycler);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.ivMenu);
        //getting Side navigation
        View headerView = navigationView.getHeaderView(0);
        Name = headerView.findViewById(R.id.user_name_side);
        Address=headerView.findViewById(R.id.address_side);
        Pfp=headerView.findViewById(R.id.pfpside);

        //getting user id
        Bundle extras = getIntent().getExtras();
        id = extras.getString("IdUser");

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeAdminActivity.this, CartActivity.class);
                intent.putExtra("IdUser",id);
                startActivity(intent);
            }
        });

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

                        Name.setText(name);
                        Address.setText(address);
                        Glide.with(HomeAdminActivity.this)
                                .load(image)
                                .into(Pfp);
                        Log.d(">>TAG", name +"\n"+address+"\n"+image);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        featuredRecycler();
        navigationDrawer();

    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void featuredRecycler() {
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.banner1, "It'll fit, trust us", ""));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.banner2, "New Style", ""));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.banner3, "Newly fashion trend", ""));

        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("IdUser",id);
                startActivity(intent);
                break;

            case R.id.Shop:
                startActivity(new Intent(getApplicationContext(), Shop_Staff.class));
                break;

            case R.id.Members:
                startActivity(new Intent(getApplicationContext(), ManagerActivity.class));
                break;

            case R.id.CreateOrder:
                intent = new Intent(getApplicationContext(), Shop.class);
                intent.putExtra("IdUser",id);
                startActivity(intent);
                break;

            case R.id.Logout:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            case R.id.Order:
                startActivity(new Intent(getApplicationContext(), OrderActivity_Staff.class));
                break;
        }
        return true;
    }

}
