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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageCategoriesFragment extends Fragment {

    static ArrayList<Category> categories;
    static ManageCategoriesListAdapter mAdapter;
    static RecyclerView mRecyclerView;
    static DatabaseReference categoriesDatabaseReference;
    EditText categoryNameEditText;
    Button addCategoryButton;

    public ManageCategoriesFragment() {
        // Required empty public constructor
    }

    public static ManageCategoriesFragment newInstance() {
        ManageCategoriesFragment fragment = new ManageCategoriesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categories = new ArrayList<>();
        categoriesDatabaseReference = FirebaseDatabase.getInstance().getReference("categories");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchCategories();
        mAdapter = new ManageCategoriesListAdapter(getContext(), categories);
        mRecyclerView = getView().findViewById(R.id.manage_categories_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryNameEditText = getView().findViewById(R.id.manage_categories_category_name_edit_text);
        addCategoryButton = getView().findViewById(R.id.manage_categories_add_category_btn);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = categoryNameEditText.getText().toString();
                Category c = new Category(categoryName);
                addCategory(c);
            }
        });
    }

    private void fetchCategories(){
        categoriesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot: snapshot.getChildren()){
                    Category category = categorySnapshot.getValue(Category.class);
                    categories.add(category);
                    Log.i("Foodiction", "category added");
                }
                if(categories.size() == 0 ) {
                    Toast.makeText(getContext(), "No Categories were found!", Toast.LENGTH_SHORT).show();
                }

                mRecyclerView.setAdapter(mAdapter);
//                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    public static void removeCategory(Category category){

        categoriesDatabaseReference.child(category.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("Foodiction", "Category was deleted!");
                categories.remove(category);
                mRecyclerView.setAdapter(mAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(mRecyclerView.getRootView(), "Error", Snackbar.LENGTH_SHORT);
            }
        });
    }

    public void addCategory(Category newCategory){
        // if not found
        for(Category category: categories){
            Log.i("Foodiction", "Category: "+ category.getName());
            if(category.getName().toLowerCase() == newCategory.getName().toLowerCase()){
                Snackbar.make(mRecyclerView.getRootView(), "Error", Snackbar.LENGTH_SHORT);
                return;
            }
        }

        categoriesDatabaseReference.child(newCategory.getId()).setValue(newCategory).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                categoryNameEditText.getText().clear();
                categories.add(newCategory);
                mRecyclerView.setAdapter(mAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error while adding category", Toast.LENGTH_SHORT).show();
            }
        });
    }
}