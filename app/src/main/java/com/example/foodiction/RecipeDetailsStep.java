package com.example.foodiction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeDetailsStep extends Fragment implements Step {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    TextView nameTextView;
    TextView descriptionTextView;
    InputMethodManager imgr;
    TextView URLTextView;
    TextInputLayout URLTextInputLayout;
    ArrayList<Category> categories;
    String[] categoriesArray;
    ArrayList<Integer> categoriesList = new ArrayList<>();
    boolean [] selectedCategories;
    DatabaseReference categoriesDatabaseReference;
    MaterialCardView selectCategoryCard;
    TextView selectedCategoriesText;
    static ImageButton selectImageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        categoriesDatabaseReference = FirebaseDatabase.getInstance().getReference("categories");

        return inflater.inflate(R.layout.fragment_recipe_details_step, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nameTextView = (TextView) getView().findViewById(R.id.details_step_recipe_name_textinput);
        descriptionTextView = (TextView) getView().findViewById(R.id.details_step_recipe_description_textinput);
        selectImageButton = getView().findViewById(R.id.details_step_choose_image_button);
        URLTextView = (TextView) getView().findViewById(R.id.details_step_recipe_internet_URL_textinput);
        URLTextInputLayout = (TextInputLayout) getView().findViewById(R.id.recipe_internet_URL_input_layout);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                getActivity().startActivityForResult(intent, PERMISSION_CODE);
            }
        });

        if (AddRecipeActivity.currentCreatedRecipe.getType() == Recipe.RecipeType.URL){
            URLTextView.setText(AddRecipeActivity.currentCreatedRecipe.getInternetUrl());
            URLTextInputLayout.setVisibility(View.VISIBLE);
        }

        nameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                AddRecipeActivity.currentCreatedRecipe.setName(charSequence.toString().toLowerCase());
                AddRecipeActivity.currentCreatedRecipe.setDisplayName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        descriptionTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                AddRecipeActivity.currentCreatedRecipe.setDescription(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        categories = new ArrayList<>();

        selectedCategoriesText = getView().findViewById(R.id.details_step_categories_text);
        fetchCategories();
        categoriesArray = new String[categories.size()];
        for(int i=0; i < categoriesArray.length; i++){
            Log.i("Foodiction", "CategoriesArray[i] = "+categoriesArray[i]);
            categoriesArray[i] = categories.get(i).getName();
        }

        selectCategoryCard = getView().findViewById(R.id.details_step_categories_cardview);
        selectedCategories = new boolean[categories.size()];
        selectCategoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoriesDialog();
            }
        });
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        // Any of the details is empty
        if (nameTextView.getText().toString().isEmpty() || descriptionTextView.getText().toString().isEmpty()){
            return new VerificationError("Enter all details!");
        }

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
//        Toast.makeText(this.getActivity(), error.getErrorMessage().toString(), Toast.LENGTH_SHORT).show();
    }

    private void showCategoriesDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose categories");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(categoriesArray, selectedCategories, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if(isChecked){
                    if(selectedCategories.length > 5){
                        Toast.makeText(getContext(), "You can't select more than 5", Toast.LENGTH_SHORT);
                    }
                    else{
                        categoriesList.add(i);
                    }
                }
                else{
                    categoriesList.remove(i);
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder stringBuilder = new StringBuilder();
                for(int j=0; j < categoriesList.size(); j++){
                    stringBuilder.append(categoriesArray[categoriesList.get(i)]);

                    if(j != categoriesList.size()){
                        stringBuilder.append(", ");
                    }
                }

                selectedCategoriesText.setText(stringBuilder.toString());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < selectedCategories.length; j++){
                    selectedCategories[i] = false;
                    categoriesList.clear();
                    selectedCategoriesText.setText("Categories");
                }
            }
        });

        builder.show();
    }

    private void fetchCategories() {
        categoriesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    categories.add(category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        categoriesArray = Arrays.copyOf(categories.toArray(), categories.size(), String[].class);
    }

//    private void fetchCategories(){
//        categoriesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot categorySnapshot: snapshot.getChildren()){
//                    Category category = categorySnapshot.getValue(Category.class);
//                    categories.add(category);
//                }
//                if(categories.size() == 0 ) {
//                    Toast.makeText(getContext(), "No Categories were found!", Toast.LENGTH_SHORT).show();
//                }
//
//                mRecyclerView.setAdapter(mAdapter);
//                mProgressBar.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                mProgressBar.setVisibility(View.INVISIBLE);
//            }
//        });
//    }
}