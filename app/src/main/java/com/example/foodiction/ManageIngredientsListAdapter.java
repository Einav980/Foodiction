package com.example.foodiction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageIngredientsListAdapter extends RecyclerView.Adapter<ManageIngredientsListAdapter.ManageIngredientViewHolder> {

    Context context;
    TextView ingredientNameTextView;
    ImageView ingredientImageView;
    ImageButton ingredientDeleteButton;
    ArrayList<Ingredient>ingredients;

    public ManageIngredientsListAdapter(Context context, ArrayList<Ingredient> ingredients){
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ManageIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.single_ingredient_item, parent, false);
        return new ManageIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageIngredientViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        ingredientNameTextView.setText(ingredients.get(holder.getAdapterPosition()).getDisplayName());
        Picasso.get().load(ingredients.get(holder.getAdapterPosition()).getImageUrl()).placeholder(R.drawable.ic_food_placeholder).into(ingredientImageView);
        ingredientDeleteButton.setVisibility(View.VISIBLE);
        ingredientDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete the ingredient?");
                builder.setTitle("Delete ingredient");

                builder.setCancelable(false);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ManageIngredientsFragment.deleteIngredient(ingredients.get(holder.getAdapterPosition()));
                        notifyItemRemoved(holder.getAdapterPosition());
                        dialog.cancel();
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
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ManageIngredientViewHolder extends RecyclerView.ViewHolder {

        public ManageIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImageView = itemView.findViewById(R.id.ingredient_image);
            ingredientNameTextView = itemView.findViewById(R.id.ingredient_name);
            ingredientDeleteButton = itemView.findViewById(R.id.single_ingredient_delete_button);
        }
    }
}

