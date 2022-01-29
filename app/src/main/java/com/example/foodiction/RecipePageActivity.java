package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class RecipePageActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    TextView titleTextView;

    ViewPager mPager;
    RecipePageTabsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        titleTextView = findViewById(R.id.Recipe_Name_Page);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                Toast.makeText(this , "Error" , Toast.LENGTH_SHORT);
            } else {
                titleTextView.setText(extras.getString("recipe_name"));
            }
        } else {
        }
        mAdapter = new RecipePageTabsAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.recipe_page_view_pager);
        mPager.setAdapter(mAdapter);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.i("Foodiction", "Tab "+ tab.getId() +" was selected");
        tab.select();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}