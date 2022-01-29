package com.example.foodiction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoriteFragment extends Fragment {
    private static RecyclerView recyclerView;
    static RecipeListAdapter adapter;
    static DatabaseReference mbase;
    private static List<Recipe> recipeListItems;
    static RecipeHandler recipeHandler;
    static MainActivity mainActivity;
    static ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = getView().findViewById(R.id.favorite_fragment_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        recipeHandler = new RecipeHandler();

        // Recycle View adapter
        mbase = FirebaseDatabase.getInstance().getReference("recipes");
        recyclerView = (RecyclerView) getView().findViewById(R.id.Recipe_List_View_Favorite);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
                .setQuery(mbase.orderByChild("is_liked").equalTo(true), Recipe.class)
                .build();

        adapter = new RecipeListAdapter(options);
        recyclerView.setAdapter(adapter);

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

    //TODO make it work
//    public static void searchByNameFavorites(String name) {
//        FirebaseRecyclerOptions<Recipe> options = new FirebaseRecyclerOptions.Builder<Recipe>()
//                .setQuery(mbase.orderByChild("name").startAt(name).endAt(name + "\uf8ff")
//                        , Recipe.class)
//                .build();
//
//        adapter.updateOptions(options);
//        recyclerView.setAdapter(adapter);
//    }

}