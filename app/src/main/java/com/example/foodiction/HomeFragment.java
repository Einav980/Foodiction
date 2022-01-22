package com.example.foodiction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static RecyclerView recyclerView;
//    private static RecyclerView.Adapter adapter;
    static RecipeListAdapter adapter;
    DatabaseReference mbase;
    private static List<Recipe> recipeListItems;
    static RecipeHandler recipeHandler;
    static MainActivity mainActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipeHandler = new RecipeHandler();
        mainActivity = new MainActivity();

        //TODO add filter logic - consider changing the home fragment to be in the main activity
        String searchInputToLower = mainActivity.getmSearchQuery().toLowerCase();
        String searchInputTOUpper = mainActivity.getmSearchQuery().toUpperCase();



        // Recycle View adapter
        mbase = FirebaseDatabase.getInstance().getReference("recipes");
        recyclerView = (RecyclerView) getView().findViewById(R.id.RecipeListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(mbase.orderByChild("name").startAt(searchInputTOUpper).endAt(searchInputToLower + "\uf8ff"), Recipe.class)
                .build();

        adapter = new RecipeListAdapter(options);
        recyclerView.setAdapter(adapter);

//        Recipe re = new Recipe("Delete", "test for UID" ,"time" );
//        addRecipeItem(re);

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
                                    Toast.makeText(recyclerView.getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            { dialog.cancel(); }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void addRecipeItem(Recipe item){
        if (recipeHandler.addRecipe(item)){
            recyclerView.setAdapter(adapter);
            Toast.makeText(recyclerView.getContext(), "Recipe added successfully", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(recyclerView.getContext(), "Could not add recipe", Toast.LENGTH_SHORT).show();
        }
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
}