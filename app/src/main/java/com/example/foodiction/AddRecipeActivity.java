package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.StepAdapter;

public class AddRecipeActivity extends FragmentActivity implements StepperLayout.StepperListener {

    ImageView imageView;
    Button chooseImageButton;
    Recipe.RecipeType recipeType;
    String URLextra;

    public static Recipe currentCreatedRecipe;

    private static final int IMAGE_PICK_CODE = 1001;
    private static final int PERMISSION_CODE = 1000;
    private StepperLayout mStepperLayout;
    private Fragment instructionsStepFragment;
    private Fragment ingredientsStepFragment;
    private Fragment detailsStepFragment;
    private RecipeHandler recipeHandler;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private ProgressDialog progressDialog;
    private Uri mImageUri;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        mAuth = FirebaseAuth.getInstance();
        // Create an empty global recipe
        currentCreatedRecipe = new Recipe();
        AddRecipeStepAdapter stepAdapterDefault = new AddRecipeStepAdapter(getSupportFragmentManager(), getApplicationContext());
        AddInternetRecipeStepAdapter stepAdapterOther = new AddInternetRecipeStepAdapter(getSupportFragmentManager(), getApplicationContext());
        imageView = findViewById(R.id.recipeImage);
        chooseImageButton = findViewById(R.id.selectImageButton);
        mStepperLayout = findViewById(R.id.addRecipeStepperLayout);
        recipeHandler = new RecipeHandler();

        mStepperLayout.setListener(this);
        recipeType = (Recipe.RecipeType) getIntent().getSerializableExtra("create_recipe_type");
        currentCreatedRecipe.setType(recipeType);
        if(recipeType == Recipe.RecipeType.URL){
            URLextra = getIntent().getStringExtra("create_recipe_internet_url");
            currentCreatedRecipe.setInternetUrl(URLextra);
        }
        if (recipeType == Recipe.RecipeType.DEFAULT){
            mStepperLayout.setAdapter(stepAdapterDefault);
        }else {
            mStepperLayout.setAdapter(stepAdapterOther);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving");

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
    }

    public void selectImage(View view){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            RecipeDetailsStep.selectImageButton.setImageURI(data.getData());
            mImageUri = data.getData();
        }
    }

    protected void onPause(){
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onCompleted(View completeButton) {
        if(mUploadTask != null && mUploadTask.isInProgress()){
            Toast.makeText(getApplicationContext(), "Saving in progress...", Toast.LENGTH_SHORT);
        }
        else
        {
            for(Ingredient i: currentCreatedRecipe.ingredients){
                if(i.getAmount().isEmpty()){
                    Snackbar.make(getCurrentFocus(), "Please provide amount in each ingredient", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
            // Upload Image and add the recipe
            UploadRecipeImage();
        }
    }

    @Override
    public void onError(VerificationError verificationError) {}

    @Override
    public void onStepSelected(int newStepPosition) {}

    @Override
    public void onReturn() {}

    @Override
    public void onBackPressed() {
        if(currentCreatedRecipe.getType() == Recipe.RecipeType.URL){
            super.onBackPressed();
            finish();
            Intent mainPage = new Intent(AddRecipeActivity.this, MainActivity.class);
            mainPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainPage);
        }
        if(currentCreatedRecipe.name.isEmpty() &&
                currentCreatedRecipe.ingredients.size() == 0 &&
                currentCreatedRecipe.instructions.size() == 0){
            super.onBackPressed();
            finish();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivity.this);
            builder.setMessage("Are you sure you want to leave? All the data will be lost!");
            builder.setTitle("Warning");

            builder.setCancelable(false);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
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
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void UploadRecipeImage(){
        if(mImageUri != null){
            progressDialog.show();
            StorageReference fileReference = mStorageRef.child(currentCreatedRecipe.getID()+"."+getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT);
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            currentCreatedRecipe.setImageUrl(uri.toString());
                            recipeHandler.addRecipe(currentCreatedRecipe);
                            progressDialog.dismiss();
                            finish();
                            Intent mainPage = new Intent(AddRecipeActivity.this, MainActivity.class);
                            mainPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainPage);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed uploading recipe", Toast.LENGTH_SHORT);
                }

            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                }
            });
        }
        else{
            progressDialog.show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recipeHandler.addRecipe(currentCreatedRecipe);
                    progressDialog.hide();
                    finish();
                }
            }, 1000);
        }
    }

}