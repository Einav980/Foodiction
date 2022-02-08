package com.example.foodiction;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

public class RecipeListAdapter extends FirebaseRecyclerAdapter<Recipe, RecipeListAdapter.ViewHolder> {
    static RecipeHandler recipeHandler;

    public RecipeListAdapter(FirebaseRecyclerOptions<Recipe> options) {
        super(options);
    }

    @NonNull
    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.ViewHolder holder, int position, @NonNull Recipe model) {
        recipeHandler = new RecipeHandler();

        holder.recipeTitle.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.makingTime.setText(model.getMakingDuration());
        holder.cardView.setClickable(true);
        if (model.getIs_liked()){
            holder.addToFavBtn.setIconResource(R.drawable.ic_baseline_favorite_24);
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.removeRecipeItem(model.getID());
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecipePageActivity.class);
                intent.putExtra("recipe_display_name", model.getDisplayName());
                intent.putExtra("recipe_description", model.getDescription());
                intent.putExtra("recipe_making_duration", model.getMakingDuration());
                intent.putExtra("recipe_ingredients", model.getIngredients());
                intent.putExtra("recipe_instructions", model.getInstructions());
                intent.putExtra("recipe_image_url", model.getImageUrl());

                v.getContext().startActivity(intent);
            }

        });

        holder.addToFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.getIs_liked()){
                    model.setIs_liked(true);
                    recipeHandler.addToFavorite(model.getID(), true);
                    holder.addToFavBtn.setIconResource(R.drawable.ic_baseline_favorite_24);
                    Toast.makeText(v.getContext(), "added to favorites", Toast.LENGTH_SHORT).show();
                }else{
                    model.setIs_liked(false);
                    recipeHandler.addToFavorite(model.getID(), false);
                    holder.addToFavBtn.setIconResource(R.drawable.ic_baseline_favorite_border_24);
                    Toast.makeText(v.getContext(), "removed from favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(! model.getImageUrl().isEmpty()){
            Picasso.get().load(model.getImageUrl()).into(holder.recipeImage);
        }
    }

    @Override
    public void onDataChanged() {
        if (HomeFragment.progressBar != null) {
            HomeFragment.progressBar.setVisibility(View.GONE);
        }

        if(FavoriteFragment.progressBar != null){
            FavoriteFragment.progressBar.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        TextView description;
        TextView makingTime;
        ImageView recipeImage;
        MaterialButton addToFavBtn;
        MaterialButton deleteBtn;
        MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeTitle = (TextView) itemView.findViewById(R.id.RecipeTitle);
            description = (TextView) itemView.findViewById(R.id.description);
            makingTime = (TextView) itemView.findViewById(R.id.makingTime);
            recipeImage = (ImageView) itemView.findViewById(R.id.RecipeImage);
            addToFavBtn = (MaterialButton) itemView.findViewById(R.id.add_to_favorite);
            deleteBtn = (MaterialButton) itemView.findViewById(R.id.DeleteBtn);
            cardView = (MaterialCardView) itemView.findViewById(R.id.card);
        }
    }
}

