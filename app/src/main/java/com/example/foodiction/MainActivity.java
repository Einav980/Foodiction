package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationBarMenu;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "FOODICTION";
    private FirebaseAuth mAuth;
    public enum GlobalMode { EDIT, VIEW }
    private DrawerLayout drawer;
    public static CharSequence foodCategories[] = {"Italian", "Asian","Meat","Home Cooking","Fish","Salads","Indian","Soups",
            "Sandwiches","Desserts", "Pastries"};
    boolean[] savedPrefrences= new boolean[foodCategories.length];
    private String mSearchQuery= "";


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
//        else{
//            mSearchQuery = savedInstanceState.getString("searchQuery");
//        }
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


    public boolean  filterByCategories (MenuItem  item){
        boolean[] prefrenses= Arrays.copyOf(savedPrefrences, foodCategories.length);;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter Categories");

        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_filter_alt_24);
        builder.setMultiChoiceItems(foodCategories, prefrenses, new DialogInterface.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            }
        });
        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO add filter logic according to the categories in the recipe
                savedPrefrences = prefrenses;
                dialog.cancel();
                Toast.makeText(MainActivity.this, "Filtered categories", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return true;
    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//
//        menuInflater.inflate(R.menu.upper_app_bar, menu);
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        if(mSearchQuery != null){
//            searchView.setIconified(true);
//            searchView.onActionViewExpanded();
//            searchView.setQuery(mSearchQuery, false);
//            searchView.setFocusable(true);
//        }
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                mSearchQuery = newText;
//                return false;
//            }
//        });
//
//        return true;
//    }


    public String getmSearchQuery() {
        return mSearchQuery;
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putString("searchQuery", mSearchQuery);
//        super.onSaveInstanceState(outState);
//    }

    public boolean searchRecipes(MenuItem  item) {
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchQuery = newText;
                return false;
            }
        });

        return true;
    }
}

