package com.example.foodiction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class RecipeImagePageActivity extends AppCompatActivity {

    ImageView recipeImage;
    String imageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_image_page);


        recipeImage = findViewById(R.id.image_recipe_page_imageView);

        if (savedInstanceState == null) {
            Bundle extras = this.getIntent().getExtras();
            if(extras == null) {
                Toast.makeText(this , "Error" , Toast.LENGTH_SHORT);
            }
            else {
                imageUri = extras.getString("recipe_image_url");
                if(imageUri != null && ! imageUri.isEmpty()){
                    Picasso.get().load(imageUri).into(recipeImage);
                }
                else
                {
                    Picasso.get().load(R.drawable.ic_food_picture).into(recipeImage);
                }
            }
        }
    }
}