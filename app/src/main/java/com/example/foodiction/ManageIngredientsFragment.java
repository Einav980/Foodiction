package com.example.foodiction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;

public class ManageIngredientsFragment extends Fragment {

    static ArrayList<Ingredient> ingredients;
    static RecyclerView mRecyclerView;
    static ManageIngredientsListAdapter mAdapter;
    EditText ingredientNameEditText;
    EditText ingredientImageUrlEditText;
    static DatabaseReference ingredientsDatabaseReference;
    Button addIngredientButton;
    ProgressBar mProgressBar;

    public ManageIngredientsFragment() {
        // Required empty public constructor
    }
    public static ManageIngredientsFragment newInstance() {
        ManageIngredientsFragment fragment = new ManageIngredientsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_ingredients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ingredients = new ArrayList<>();
        mRecyclerView = getView().findViewById(R.id.manage_ingredients_recycler_view);
        mRecyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        mProgressBar = getView().findViewById(R.id.manage_ingredients_progress_bar);
        addIngredientButton = getView().findViewById(R.id.manage_ingredients_add_ingredient_btn);
        ingredientNameEditText = getView().findViewById(R.id.manage_ingredients_name_edit_text);
        ingredientImageUrlEditText = getView().findViewById(R.id.manage_ingredients_image_url);
        mProgressBar.setVisibility(View.VISIBLE);
        fetchIngredients();
        mAdapter = new ManageIngredientsListAdapter(getContext(), ingredients);
        mRecyclerView.setAdapter(mAdapter);

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingredientName = ingredientNameEditText.getText().toString();
                String ingredientImageUrl = ingredientImageUrlEditText.getText().toString();
                if(ingredientName.isEmpty() || ingredientImageUrl.isEmpty()){
                    Snackbar.make(getView(), "You must enter Name and ImageUrl", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    if(URLUtil.isValidUrl(ingredientImageUrl)){
                        Ingredient i = new Ingredient(ingredientName, ingredientImageUrl);
                        addIngredient(i);
                    }
                    else
                    {
                        Snackbar.make(getView(), "Please enter a valid URL", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void fetchIngredients(){
        ingredientsDatabaseReference = FirebaseDatabase.getInstance().getReference("ingredients");
        ingredientsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ingredientSnapshot: snapshot.getChildren()){
                    Ingredient i = ingredientSnapshot.getValue(Ingredient.class);
                    ingredients.add(i);
                }
                mProgressBar.setVisibility(View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addIngredient(Ingredient newIngredient){
        for(Ingredient ingredient: ingredients){
            if(ingredient.getName().equalsIgnoreCase(newIngredient.getName())){
                Snackbar.make(getView(), "Ingredient already added!", Snackbar.LENGTH_SHORT).show();
                return;
            }
        }

        ingredientsDatabaseReference.child(newIngredient.getId()).setValue(newIngredient).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ingredientNameEditText.getText().clear();
                ingredientImageUrlEditText.getText().clear();
                ingredients.add(newIngredient);
                mAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error while adding category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void deleteIngredient(Ingredient ingredient){
        ingredientsDatabaseReference.child(ingredient.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Snackbar.make(mRecyclerView, String.format("Ingredient '%s' was deleted", ingredient.getName()), Snackbar.LENGTH_SHORT).show();
                ingredients.remove(ingredient);
                mAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(mRecyclerView, "Error", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}