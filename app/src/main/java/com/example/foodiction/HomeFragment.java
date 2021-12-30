package com.example.foodiction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter adapter;
    private static List<Recipe> recipeListItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recycle View adapter
        recyclerView = (RecyclerView) getView().findViewById(R.id.RecipeListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeListItems = new ArrayList<>();

        adapter = new RecipeListAdapter(recipeListItems , getContext());
        recyclerView.setAdapter(adapter);
    }

    public static void removeRecipeItem(int remove){

        AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
        builder.setMessage("Are you sure you want to delete the recipe?");
        builder.setTitle("Delete recipe");

        builder.setCancelable(false);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                recipeListItems.remove(remove);
                                recyclerView.setAdapter(adapter);
                                dialog.cancel();
                                Toast.makeText(recyclerView.getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
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

    public static void addRecipeItem(Recipe item){
        recipeListItems.add(item);
        recyclerView.setAdapter(adapter);
    }
}