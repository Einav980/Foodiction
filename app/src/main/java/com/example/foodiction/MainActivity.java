package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "FOODICTION";
    private FirebaseAuth mAuth;
    public enum GlobalMode { EDIT, VIEW }
    private DrawerLayout drawer;
    public String userGuid;

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

        initUserGuid();
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

    public String getGUID(){
        return java.util.UUID.randomUUID().toString();
    }

    public void setFirstTimeUseGUID(){
        String state = Environment.getExternalStorageState();
        if (isExternalStorageWritable()) {
            File file = new File(getExternalFilesDir("Foodiction"), "user_guid.txt");
            FileOutputStream outputStream = null;
            try {
                String guid = getGUID();
                file.createNewFile();

                outputStream = new FileOutputStream(file, true);
                outputStream.write(guid.getBytes());
                outputStream.flush();
                outputStream.close();

                this.userGuid = guid;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Problem!", Toast.LENGTH_LONG);
        }
    }

    public void initUserGuid(){
        String guid = null;
        File guidFile = new File(getExternalFilesDir("Foodiction"), "user_guid.txt");
        // If already exists
        if(guidFile.exists()){
            guid = getFileData(guidFile);
            if(!guid.isEmpty()){
                Log.d("Foodiction", "Retrieved guid");
                this.userGuid = guid;
            }
        }
        else
        {
            Log.d("Foodiction", "Initialising guid");
            setFirstTimeUseGUID();
        }
    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private String getFileData(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            int i = -1;
            StringBuffer buffer = new StringBuffer();
            while ((i = fileInputStream.read()) != -1) {
                buffer.append((char) i);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

