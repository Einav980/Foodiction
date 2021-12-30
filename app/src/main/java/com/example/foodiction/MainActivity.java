package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "FOODICTION";
    private FirebaseAuth mAuth;
    public enum GlobalMode { EDIT, VIEW }
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        MaterialToolbar topApp = findViewById(R.id.main_toolbar);


        // TopBar Listener
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, topApp,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Initial fragment in MainActivity
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.Home_page_btn);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    public void loginFunc() {
        String email = "test@example.com";
        String password = "1234";
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login successfull", Toast.LENGTH_LONG).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void registerFunc() {
        String email = "test@example.com";
        String password = "1234";
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Registered Failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addData() {

        Recipe r = new Recipe("Test", "This is test recipe", "10min");

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipes = database.getReference("recipes");
        String key = recipes.push().getKey();
        recipes.child(key).setValue(r);
        Toast.makeText(MainActivity.this, "A recipe was added!", Toast.LENGTH_LONG).show();
    }

    public void getData() {
        Recipe r = new Recipe("First Recipe", "This is the first recipe", "10min");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipes").child(r.getName());

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recipe value = dataSnapshot.getValue(Recipe.class);
                Toast.makeText(MainActivity.this, value.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    ValueEventListener queryValueListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
            Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

            while (iterator.hasNext()) {
                DataSnapshot next = (DataSnapshot) iterator.next();
                Log.i(TAG, "Value = " + next.child("name").getValue());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void listRecipes() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipesRef = database.getReference("recipes");
        Query query = recipesRef.orderByKey();
        query.addListenerForSingleValueEvent(queryValueListener);
    }

    public void deleteRecipe() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipesRef = database.getReference("recipes");
        recipesRef.child("-MqBCr8dOUpl1eIC6I3o").removeValue();
    }

    public void startRecipeActivity(View view) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MaterialToolbar topApp = findViewById(R.id.main_toolbar);

        switch (item.getItemId()) {
            case R.id.Profile_btn:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                setAppBarOptionsVisibility(View.INVISIBLE,View.INVISIBLE );
                topApp.setTitle("Profile");
                break;
            case R.id.Settings_btn:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                setAppBarOptionsVisibility(View.INVISIBLE,View.INVISIBLE );
                topApp.setTitle("Settings");
                break;
            case R.id.Home_page_btn:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                setAppBarOptionsVisibility(View.VISIBLE,View.VISIBLE );
                topApp.setTitle("Foodiction");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    void setAppBarOptionsVisibility (int filterVisibilty, int SearchVisibility){
        findViewById(R.id.filter).setVisibility(filterVisibilty);
        findViewById(R.id.search).setVisibility(SearchVisibility);
    }
}

