package com.example.foodiction;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private List<Recipe> recipeListItems;
    private Context context;

    public RecipeListAdapter(List<Recipe> recipeListItems, Context context) {
        this.recipeListItems = recipeListItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.ViewHolder holder, int position) {
        Recipe recipeListItem = recipeListItems.get(position);

        holder.recipeTitle.setText(recipeListItem.getName());
        holder.description.setText(recipeListItem.getDescription());
        holder.makingTime.setText(recipeListItem.getMakingDuration());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment.removeRecipeItem(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeTitle;
        TextView description;
        TextView makingTime;
        ImageView recipeImage;
        MaterialButton addToFavBtn;
        MaterialButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeTitle = (TextView) itemView.findViewById(R.id.RecipeTitle);
            description = (TextView) itemView.findViewById(R.id.description);
            makingTime = (TextView) itemView.findViewById(R.id.makingTime);
            recipeImage = (ImageView) itemView.findViewById(R.id.RecipeImage);
            addToFavBtn = (MaterialButton) itemView.findViewById(R.id.AddToFav);
            deleteBtn = (MaterialButton) itemView.findViewById(R.id.DeleteBtn);

        }
    }

}

