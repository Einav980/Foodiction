package com.example.foodiction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Observable;


public class HomeFragment extends Fragment {

    private static RecyclerView recyclerView;
    static RecipeListAdapter adapter;
    static DatabaseReference recipesDatabaseReference;
    static RecipeHandler recipeHandler;
    static ProgressBar progressBar;
    ArrayList<Recipe> recipes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipeHandler = new RecipeHandler();
        recipes = new ArrayList<>();
        // Recycle View adapter
        recipesDatabaseReference = FirebaseDatabase.getInstance().getReference("recipes");
        recyclerView = (RecyclerView) getView().findViewById(R.id.RecipeListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(recipesDatabaseReference.orderByChild("name"), Recipe.class)
                .build();

        adapter = new RecipeListAdapter(options);

        progressBar = getView().findViewById(R.id.home_fragment_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setAdapter(adapter);
    }

    public static void removeRecipeItem(String recipeUUID){

        AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
        builder.setMessage("Are you sure you want to delete the recipe?");
        builder.setTitle("Delete recipe");

        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (recipeHandler.deleteRecipe(recipeUUID)){
                    recyclerView.setAdapter(adapter);
                    dialog.cancel();
                    Toast.makeText(recyclerView.getContext(), "Recipe has been deleted successfully", Toast.LENGTH_SHORT).show();
                }
            }}).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
                { dialog.cancel(); }
            });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static void searchByName(String name) {
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(recipesDatabaseReference.orderByChild("name").startAt(name.toLowerCase()).endAt(name.toLowerCase() + "\uf8ff".toLowerCase()), Recipe.class)
                .build();

        adapter.updateOptions(options);
        recyclerView.setAdapter(adapter);
    }

    public static void filterByCategories(Category category) {
        ArrayList<Recipe> filteredRecipes = new ArrayList<>();
        Log.i("Foodiction", "Filtered");
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(recipesDatabaseReference.orderByChild("category").equalTo(category.getName()), Recipe.class)
                .build();
        adapter.updateOptions(options);
        recyclerView.setAdapter(adapter);
    }

    public static void clearFilter() {
        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(recipesDatabaseReference.orderByChild("name"), Recipe.class)
                .build();
        adapter.updateOptions(options);
        recyclerView.setAdapter(adapter);
    }
}