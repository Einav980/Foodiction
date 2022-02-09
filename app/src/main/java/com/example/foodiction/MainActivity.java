package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "FOODICTION";
    private FirebaseAuth mAuth;

    public enum GlobalMode { EDIT, VIEW }
    private DrawerLayout drawer;
    public static CharSequence foodCategories[] = {"Italian", "Asian","Meat","Home Cooking","Fish","Salads","Indian","Soups",
            "Sandwiches","Desserts", "Pastries"};
    int savedPreferences = -1;
    private String mSearchQuery= "";
    private static String userGuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        MaterialToolbar topBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(topBar);

        // TopBar Listener
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, topBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Initial fragment in MainActivity
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home_page_menu_item);
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
    }


    public void startRecipeActivity(View view) {
        Intent intent = new Intent(this, ChooseRecipeTypeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MaterialToolbar topApp = findViewById(R.id.main_toolbar);

        switch (item.getItemId()) {
            case R.id.favorite_recipes_menu_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoriteFragment()).commit();
                topApp.findViewById(R.id.search).setVisibility(View.INVISIBLE); //delete is needed
                topApp.setTitle("Favorites");
                break;
            case R.id.home_page_menu_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                topApp.setTitle("Recipes");
                topApp.findViewById(R.id.search).setVisibility(View.VISIBLE);
                break;
            case R.id.manage_ingredients_menu_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageIngredientsFragment()).commit();
                topApp.setTitle("Manage Ingredients");
                topApp.findViewById(R.id.search).setVisibility(View.INVISIBLE);
                break;
            case R.id.manage_categories_menu_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageCategoriesFragment()).commit();
                topApp.setTitle("Manage Categories");
                topApp.findViewById(R.id.search).setVisibility(View.INVISIBLE);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String generateGUID(){
        return java.util.UUID.randomUUID().toString();
    }

    public void setFirstTimeUseGUID(){
        String state = Environment.getExternalStorageState();
        if (isExternalStorageWritable()) {
            File file = new File(getExternalFilesDir("Foodiction"), "user_guid.txt");
            FileOutputStream outputStream = null;
            try {
                String guid = generateGUID();
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

    public static String getUserGuid() {
        return userGuid;
    }

    public boolean  filterByCategories (MenuItem  item){
//        boolean[] prefrenses= Arrays.copyOf(savedPrefrences, foodCategories.length);
        int[] preferences = {savedPreferences};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter Categories");

        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_filter_alt_24);
        builder.setSingleChoiceItems(foodCategories, preferences[0], new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences[0] = which;
            }
        });
        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO add filter logic according to the categories in the recipe
//                savedPrefrences = prefrenses[0];
                HomeFragment.filterByCategories(savedPreferences);
                dialog.cancel();
                Toast.makeText(MainActivity.this, "Filtered category", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO add filter logic according to the categories in the recipe
                savedPreferences = -1;
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upper_app_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof HomeFragment) {
                    Log.i("search", "Search in current fragment: HomeFragment");
                    HomeFragment.searchByName(newText);
                }
                return false;
            }
        });

        return true;
    }

}

