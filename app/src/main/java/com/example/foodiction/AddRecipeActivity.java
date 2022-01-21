package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.StepAdapter;

public class AddRecipeActivity extends AppCompatActivity {

    ImageView imageView;
    Button chooseImageButton;

    public static Recipe currentCreatedRecipe;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private StepperLayout mStepperLayout;
    private Fragment instructionsStepFragment;
    private Fragment ingredientsStepFragment;
    private Fragment detailsStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Create an empty global recipe
        currentCreatedRecipe = new Recipe();

        imageView = findViewById(R.id.recipeImage);
        chooseImageButton = findViewById(R.id.selectImageButton);

        mStepperLayout = findViewById(R.id.addRecipeStepperLayout);
        AddRecipeStepAdapter stepAdapter = new AddRecipeStepAdapter(getSupportFragmentManager(), getApplicationContext());
        mStepperLayout.setAdapter(stepAdapter);

        if(savedInstanceState != null){
            instructionsStepFragment = getSupportFragmentManager().getFragment(savedInstanceState, "instructionsStepFragment");
            ingredientsStepFragment = getSupportFragmentManager().getFragment(savedInstanceState, "ingredientsStepFragment");
            detailsStepFragment = getSupportFragmentManager().getFragment(savedInstanceState, "detailsStepFragment");
        }
    }

    public void selectImage(View view){
        Log.i("FOODICTION", "Select Image");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                // Permission not granted - request it
                String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            }
            else{
                // Permission granted
                pickImageFromGallery();
            }
        }
        else{
            pickImageFromGallery();
        }
    }

    public void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//    public void addRecipe(View view) {
//        String recipeName = ((TextView) findViewById(R.id.recipeNameTextBox)).getText().toString();
//        String recipeDescription = ((TextView) findViewById(R.id.recipeDescTextBox)).getText().toString();
//        Recipe newRecipe = new Recipe(recipeName, recipeDescription , "10min");
//        boolean result = recipeHandler.addRecipe(newRecipe);
//        if (result) {
//            HomeFragment.addRecipeItem(newRecipe);
//            Toast.makeText(this, "Recipe added succesfully!", Toast.LENGTH_SHORT).show();
//            this.finish();
//        } else {
//            Toast.makeText(this, "There was an error!", Toast.LENGTH_SHORT).show();
//        }
//    }


//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList("currentCreatedRecipe_ingredients", currentCreatedRecipe.getIngredients());
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        savedInstanceState.getPar
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageView.setImageURI(data.getData());
        }
    }

    protected void onPause(){
        super.onPause();
        Log.i("FOODICTION", "Recipe stopped");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}