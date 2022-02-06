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
import android.widget.Toast;

import java.util.ArrayList;

public class RecipePageIngredientsFragment extends Fragment {

    ArrayList<Ingredient> ingredients;
    AddedIngredientListAdapter mAdapter;
    RecyclerView mRecyclerView;
    public RecipePageIngredientsFragment() {
        // Required empty public constructor
    }

    public static RecipePageIngredientsFragment newInstance() {
        RecipePageIngredientsFragment fragment = new RecipePageIngredientsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_page_ingredients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            Bundle extras = getActivity().getIntent().getExtras();
            if(extras == null) {
                Toast.makeText(getActivity() , "Error" , Toast.LENGTH_SHORT);
            }
            else {
                ingredients = extras.getParcelableArrayList("recipe_ingredients");
            }
        }

        mAdapter = new AddedIngredientListAdapter(ingredients, getContext(), MainActivity.GlobalMode.VIEW);
        mRecyclerView = getView().findViewById(R.id.recipe_page_ingredients_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(mAdapter);
    }
}