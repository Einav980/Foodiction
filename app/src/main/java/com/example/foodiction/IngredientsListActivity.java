package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IngredientsListActivity extends AppCompatActivity {
    static RecyclerView ingredientsRecyclerView;
    ProgressBar mProgressCircle;
    SearchView mSearchView;
    DatabaseReference mBase;
    IngredientsListAdapter adapter;


    public class IngredientComparator implements Comparator<Ingredient>
    {
        public int compare(Ingredient left, Ingredient right) {
            return left.getName().compareTo(right.getName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_list);

        mSearchView = findViewById(R.id.ingredients_search_edittext);
        mProgressCircle = findViewById(R.id.ingredientListProgressCircle);

        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBase = FirebaseDatabase.getInstance().getReference("ingredients");
        FirebaseRecyclerOptions<Ingredient> options = new FirebaseRecyclerOptions.Builder<Ingredient>().setQuery(mBase.orderByChild("name"), Ingredient.class).build();

        adapter = new IngredientsListAdapter(options, this, MainActivity.GlobalMode.VIEW);
        ingredientsRecyclerView.setAdapter(adapter);

        mProgressCircle.setVisibility(View.INVISIBLE);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mBase = FirebaseDatabase.getInstance().getReference("ingredients");
                FirebaseRecyclerOptions<Ingredient> options = new FirebaseRecyclerOptions.Builder<Ingredient>()
                        .setQuery(mBase.orderByChild("name").startAt(newText.toLowerCase()).endAt(newText.toLowerCase() + "\uf8ff".toLowerCase()), Ingredient.class).build();

                adapter.updateOptions(options);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressCircle.setVisibility(View.VISIBLE);
    }
}