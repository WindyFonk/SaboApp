import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.splashscreen.adapter.FeaturedAdapter;
import com.example.splashscreen.adapter.FeaturedHelperClass;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    RecyclerView featuredRecycler;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        featuredRecycler = findViewById(R.id.featured_recycler);

        featuredRecycler();
    }

    private void featuredRecycler() {
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.fitness_new,"Want a perfect body? Read this","Copyright by author"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.girl,"New Style","Copyright by author"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.cats,"Dayly dose of cat","Copyright by author"));

        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter);
    }
}