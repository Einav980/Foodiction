package com.example.foodiction;

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

public class RecipeListAdapter extends FirebaseRecyclerAdapter<Recipe, RecipeListAdapter.ViewHolder> {
    static RecipeHandler recipeHandler;
//    private List<Recipe> recipeListItems;
//    private Context context;

//    public RecipeListAdapter(List<Recipe> recipeListItems, Context context) {
//        this.recipeListItems = recipeListItems;
//        this.context = context;
//    }

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
//        Recipe recipeListItem = recipeListItems.get(position);
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
                Toast.makeText(v.getContext(), "Selected Card", Toast.LENGTH_SHORT).show();
            }
        });

        holder.addToFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.getIs_liked()){
                    recipeHandler.add2favoriteRecipe(model.getID(), true);
                    model.setIs_liked(true);
                    holder.addToFavBtn.setIconResource(R.drawable.ic_baseline_favorite_24);
                    Toast.makeText(v.getContext(), "added to favorites", Toast.LENGTH_SHORT).show();
                }else{
                    recipeHandler.add2favoriteRecipe(model.getID(), false);
                    model.setIs_liked(false);
                    holder.addToFavBtn.setIconResource(R.drawable.ic_baseline_favorite_border_24);
                    Toast.makeText(v.getContext(), "removed from favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

