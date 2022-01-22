package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
    static ArrayList<Ingredient> ingredientsList;
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
        FirebaseRecyclerOptions<Ingredient> options= new FirebaseRecyclerOptions.Builder<Ingredient>().setQuery(mBase, Ingredient.class).build();

        adapter = new IngredientsListAdapter(options, this);
        ingredientsRecyclerView.setAdapter(adapter);

        mProgressCircle.setVisibility(View.INVISIBLE);
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchAllIngredients(ArrayList<Ingredient> list){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ingredients");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ingredientSnapshot: snapshot.getChildren()){
                    Ingredient ingredient = ingredientSnapshot.getValue(Ingredient.class);
                    list.add(ingredient);
                }
                Collections.sort(list, new IngredientComparator());
                if(list.size() == 0 ) {
                    Toast.makeText(IngredientsListActivity.this, "No ingredients were found!", Toast.LENGTH_SHORT).show();
                }
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