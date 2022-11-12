package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adapter.FeaturedAdapter;
import com.example.adapter.FeaturedHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView featuredRecycler;
    RecyclerView.Adapter adapter;
    ImageView menuIcon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView Name, Address;
    CircleImageView Pfp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        featuredRecycler = findViewById(R.id.featured_recycler);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_customer);
        menuIcon = findViewById(R.id.ivMenu);

        //getting Side navigation
        View headerView = navigationView.getHeaderView(0);
        Name = headerView.findViewById(R.id.user_name_side);
        Address=headerView.findViewById(R.id.address_side);
        Pfp=headerView.findViewById(R.id.pfpside);

        //getting user id
        SharedPreferences sharedPreferences =
                getSharedPreferences("USER",MODE_PRIVATE);
        String id = sharedPreferences.getString("Id",null);

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
                        Glide.with(HomeActivity.this)
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

        //User information on navigation side



        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
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

        featuredLocations.add(new FeaturedHelperClass(R.drawable.fitness_new, "Want a perfect body? Read this", "Copyright by author"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.girl, "New Style", "Copyright by author"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.cats, "Dayly dose of cat", "Copyright by author"));

        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Profile:
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            break;

            case R.id.shop:
                startActivity(new Intent(getApplicationContext(), Shop.class));
                break;
        }
        return true;
    }
}