package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

public class RecipePageActivity extends AppCompatActivity {
    TextView titleTextView;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    RecipePageTabsAdapter adapter;
    ImageView recipeImage;
    String imageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_page);

        tabLayout = findViewById(R.id.recipe_page_tab_layout);
        viewPager2 = findViewById(R.id.recipe_page_view_pager);
        recipeImage = findViewById(R.id.recipe_page_image_view);

        if (savedInstanceState == null) {
            Bundle extras = this.getIntent().getExtras();
            if(extras == null) {
                Toast.makeText(this , "Error" , Toast.LENGTH_SHORT);
            }
            else {
                imageUri = extras.getString("recipe_image_url");
                if(imageUri != null){
                    Picasso.get().load(imageUri).into(recipeImage);
                }
                else
                {
                    Picasso.get().load(R.drawable.ic_food_picture).into(recipeImage);
                }
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        adapter = new RecipePageTabsAdapter(fm, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}