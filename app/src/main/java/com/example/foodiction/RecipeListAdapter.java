package com.example.foodiction;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        holder.setIsRecyclable(false);
        recipeHandler = new RecipeHandler();
        holder.recipeTypeIcon.setClickable(false);
        holder.recipeTitle.setText(model.getName());
        holder.description.setText(model.getDescription());
        holder.makingTime.setText(model.getMakingDuration());
        holder.cardView.setClickable(true);
        if (model.getIs_liked()){
            holder.addToFavBtn.setIconResource(R.drawable.ic_baseline_favorite_24);
        }
        else{
            holder.addToFavBtn.setIconResource(R.drawable.ic_baseline_favorite_border_24);
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
                Intent intent;
                if (model.getType() == Recipe.RecipeType.DEFAULT){
                    intent = new Intent(v.getContext(), RecipePageActivity.class);
                    intent.putExtra("recipe_display_name", model.getDisplayName());
                    intent.putExtra("recipe_description", model.getDescription());
                    intent.putExtra("recipe_making_duration", model.getMakingDuration());
                    intent.putExtra("recipe_ingredients", model.getIngredients());
                    intent.putExtra("recipe_instructions", model.getInstructions());
                    intent.putExtra("recipe_image_url", model.getImageUrl());
                    intent.putExtra("recipe_category", model.getCategory());
                    v.getContext().startActivity(intent);
                }else if(model.getType() == Recipe.RecipeType.URL){
                    intent = new Intent(v.getContext(), RecipeInternetPageActivity.class);
                    intent.putExtra("recipe_internet_url", model.getInternetUrl());
                    v.getContext().startActivity(intent);
                } else if(model.getType() == Recipe.RecipeType.IMAGE){
                    intent = new Intent(v.getContext(), RecipeImagePageActivity.class);
                    intent.putExtra("recipe_image_url", model.getImageUrl());
                    v.getContext().startActivity(intent);
                }
            }
        });

        holder.addToFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.getIs_liked()){
                    recipeHandler.addToFavorite(model.getID(), true);
                    holder.addToFavBtn.setIconResource(R.drawable.ic_baseline_favorite_24);
                    Toast.makeText(v.getContext(), String.format("Recipe %s has been added to favorites!", model.getDisplayName()), Toast.LENGTH_SHORT).show();
                }else{
                    recipeHandler.addToFavorite(model.getID(), false);
                    holder.addToFavBtn.setIconResource(R.drawable.ic_baseline_favorite_border_24);
                    Toast.makeText(v.getContext(), String.format("Recipe %s has been removed from favorites", model.getDisplayName()), Toast.LENGTH_SHORT).show();
                }
                model.toggle_like();
            }
        });

        if(model.getImageUrl() != null && ! model.getImageUrl().isEmpty()){
            Picasso.get().load(model.getImageUrl()).into(holder.recipeImage);
        }

        if(model.getType() == Recipe.RecipeType.DEFAULT){
            holder.recipeTypeIcon.setVisibility(View.INVISIBLE);
        }
        if(model.getType() == Recipe.RecipeType.URL){
            holder.recipeTypeIcon.setVisibility(View.VISIBLE);
            holder.recipeTypeIcon.setImageResource(R.drawable.ic_baseline_link_24);
        }
        if(model.getType() == Recipe.RecipeType.IMAGE){
            holder.recipeTypeIcon.setVisibility(View.VISIBLE);
            holder.recipeTypeIcon.setImageResource(R.drawable.ic_baseline_image_24);
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
        ImageButton recipeTypeIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeTitle = (TextView) itemView.findViewById(R.id.RecipeTitle);
            description = (TextView) itemView.findViewById(R.id.description);
            makingTime = (TextView) itemView.findViewById(R.id.single_recipe_making_duration);
            recipeImage = (ImageView) itemView.findViewById(R.id.RecipeImage);
            addToFavBtn = (MaterialButton) itemView.findViewById(R.id.single_recipe_add_to_fav_button);
            deleteBtn = (MaterialButton) itemView.findViewById(R.id.single_recipe_delete_button);
            cardView = (MaterialCardView) itemView.findViewById(R.id.card);
            recipeTypeIcon = itemView.findViewById(R.id.single_recipe_type_indicator);
        }
    }
}

