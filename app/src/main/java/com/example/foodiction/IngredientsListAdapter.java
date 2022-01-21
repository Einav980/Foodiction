package com.example.foodiction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class IngredientsListAdapter extends FirebaseRecyclerAdapter<Ingredient, IngredientsListAdapter.IngredientViewHolder> {
    Context context;
    Activity mActivity;

    public IngredientsListAdapter(
            @NonNull FirebaseRecyclerOptions<Ingredient> options, Activity mActivity){
        super(options);
        this.mActivity = mActivity;
    }

    @Override
    protected void
    onBindViewHolder(@NonNull IngredientViewHolder holder,
                     int position, @NonNull Ingredient model)
    {

        Picasso.get().load(model.getImageUrl()).placeholder(R.mipmap.ic_launcher_round).into(holder.ingredientImageView);
        holder.ingredientNameTextView.setText(model.getName());
        holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();

                resultIntent.putExtra("ingredientName", model.getName());
                resultIntent.putExtra("ingredientImage",model.getImageUrl());
                resultIntent.putExtra("ingredientAmount", model.getAmount());
                mActivity.setResult(IngredientsListActivity.RESULT_OK, resultIntent);
                mActivity.finish();
            }
        });
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

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImageView = itemView.findViewById(R.id.ingredientImage);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientName);
        }
    }
}
