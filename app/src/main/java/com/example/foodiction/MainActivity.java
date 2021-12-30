package com.example.foodiction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FOODICTION";
    private Toolbar mToolBar;
    private FirebaseAuth mAuth;
    public enum GlobalMode { EDIT, VIEW }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    public void loginFunc() {
        String email = "test@example.com";
        String password = "1234";
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Login successfull", Toast.LENGTH_LONG).show();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void registerFunc(){
        String email = "test@example.com";
        String password = "1234";
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Registered successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,"Registered Failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addData(){

        Recipe r = new Recipe("Test", "This is test recipe");

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipes = database.getReference("recipes");
        String key = recipes.push().getKey();
        recipes.child(key).setValue(r);
        Toast.makeText(MainActivity.this, "A recipe was added!", Toast.LENGTH_LONG).show();
    }

    public void getData(){
        Recipe r = new Recipe("First Recipe", "This is the first recipe");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipes").child(r.getName());

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recipe value = dataSnapshot.getValue(Recipe.class);
                Toast.makeText(MainActivity.this, value.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    ValueEventListener queryValueListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
            Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

            while (iterator.hasNext()) {
                DataSnapshot next = (DataSnapshot) iterator.next();
                Log.i(TAG, "Value = " + next.child("name").getValue());
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void listRecipes(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipesRef = database.getReference("recipes");
        Query query = recipesRef.orderByKey();
        query.addListenerForSingleValueEvent(queryValueListener);
    }

    public void deleteRecipe(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference recipesRef = database.getReference("recipes");
        recipesRef.child("-MqBCr8dOUpl1eIC6I3o").removeValue();
    }

    public void startRecipeActivity(View view) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

