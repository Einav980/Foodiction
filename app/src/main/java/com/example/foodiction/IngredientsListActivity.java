package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class IngredientsListActivity extends AppCompatActivity {
    ArrayList<Ingredient> ingredientsList;
    ListView ingredientsListView;
    ProgressBar mProgressCircle;
    SearchView mSearchView;
    IngredientsListAdapter adapter;

    public class IngredientComparator implements Comparator<Ingredient>
    {
        public int compare(Ingredient left, Ingredient right) {
            return left.getName().compareTo(right.getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_list);

        mProgressCircle = findViewById(R.id.ingredientListProgressCircle);
        ingredientsList = new ArrayList<>();
        ingredientsListView = findViewById(R.id.ingredientsListView);
        mSearchView = findViewById(R.id.ingredientSerachEditText);
        // Get all ingredients from DB
        fetchAllIngredients();

        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent resultIntent = new Intent();

                resultIntent.putExtra("ingredientName", ingredientsList.get(i).getName());
                resultIntent.putExtra("ingredientImage", ingredientsList.get(i).getImageUrl());
                resultIntent.putExtra("ingredientAmount", ingredientsList.get(i).getAmount());
                setResult(IngredientsListActivity.RESULT_OK, resultIntent);
                finish();
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                IngredientsListAdapter adapter = new IngredientsListAdapter(getApplicationContext(), ingredientsList);
                adapter.getFilter().filter(s);
                ingredientsListView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                IngredientsListAdapter adapter = new IngredientsListAdapter(getApplicationContext(), ingredientsList);
                adapter.getFilter().filter(s);
//                ingredientsListView.setAdapter(adapter);
                return false;
            }
        });
    }

    private void fetchAllIngredients(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ingredients");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ingredientSnapshot: snapshot.getChildren()){
                    Ingredient ingredient = ingredientSnapshot.getValue(Ingredient.class);
                    ingredientsList.add(ingredient);
                }
                Collections.sort(ingredientsList, new IngredientComparator());
                if(ingredientsList.size() == 0 ) {
                    Toast.makeText(IngredientsListActivity.this, "No ingredients were found!", Toast.LENGTH_SHORT).show();
                }
                adapter = new IngredientsListAdapter(getApplicationContext(), ingredientsList);
                ingredientsListView.setAdapter(adapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(IngredientsListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressCircle.setVisibility(View.VISIBLE);
    }
}