package com.example.foodiction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class IngredientsListAdapter extends FirebaseRecyclerAdapter<Ingredient, IngredientsListAdapter.IngredientViewHolder> {
    Context context;
    Activity mActivity;
    MainActivity.GlobalMode mode;

    public IngredientsListAdapter(
            @NonNull FirebaseRecyclerOptions<Ingredient> options, Activity mActivity, MainActivity.GlobalMode mode){
        super(options);
        this.mActivity = mActivity;
        this.mode = mode;
    }

    @Override
    protected void
    onBindViewHolder(@NonNull IngredientViewHolder holder,
                     int position, @NonNull Ingredient model)
    {

        Picasso.get().load(model.getImageUrl()).placeholder(R.drawable.ic_food_placeholder).into(holder.ingredientImageView);
        holder.ingredientNameTextView.setText(model.getDisplayName());
        holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ingredientObject", model);
                mActivity.setResult(IngredientsListActivity.RESULT_OK, resultIntent);
                mActivity.finish();
            }
        });
        if(mode == MainActivity.GlobalMode.EDIT){
            holder.deleteButton.setVisibility(View.VISIBLE);
        }
        else{
            holder.deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_ingredient_item, parent, false);
        return new IngredientsListAdapter.IngredientViewHolder(view);
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientNameTextView;
        ImageView ingredientImageView;
        ImageButton deleteButton;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImageView = itemView.findViewById(R.id.ingredient_image);
            ingredientNameTextView = itemView.findViewById(R.id.ingredient_name);
            deleteButton = itemView.findViewById(R.id.single_ingredient_delete_button);
        }
    }
}
