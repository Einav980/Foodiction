package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
    View mView;

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
        recipeHandler = new RecipeHandler();

        imageView = findViewById(R.id.recipeImage);
        chooseImageButton = findViewById(R.id.selectImageButton);

        mStepperLayout = findViewById(R.id.addRecipeStepperLayout);
        mStepperLayout.setListener(this);
        AddRecipeStepAdapter stepAdapter = new AddRecipeStepAdapter(getSupportFragmentManager(), getApplicationContext());
        mStepperLayout.setAdapter(stepAdapter);

        if(savedInstanceState != null){
            instructionsStepFragment = getSupportFragmentManager().getFragment(savedInstanceState, "instructionsStepFragment");
            ingredientsStepFragment = getSupportFragmentManager().getFragment(savedInstanceState, "ingredientsStepFragment");
            detailsStepFragment = getSupportFragmentManager().getFragment(savedInstanceState, "detailsStepFragment");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving");

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
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
        Log.i("FOODICTION", "Recipe stopped");
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onCompleted(View completeButton) {
        if(mUploadTask != null && mUploadTask.isInProgress()){
            Toast.makeText(getApplicationContext(), "Saving in progress...", Toast.LENGTH_SHORT);
        }
        else
        {
            UploadRecipeImage();
        }
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        Toast.makeText(getApplicationContext(), "Returned", Toast.LENGTH_SHORT).show();
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
                            progressDialog.hide();
                            Log.i("Foodiction", "recipe: "+ currentCreatedRecipe.toString());
                            finish();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed uploading recipe", Toast.LENGTH_SHORT);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    Log.i("Foodiction", "Started uploading...");
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Log.i("Foodiction", task.getResult().getStorage().getDownloadUrl().toString());
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "You must select an image for the recipe!", Toast.LENGTH_SHORT).show();
        }
    }

}