package com.example.foodiction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddedIngredientListAdapter extends RecyclerView.Adapter<AddedIngredientListAdapter.AddedIngredientViewHolder> {

    Context context;
    List<Ingredient> addedIngredients;
    TextView addedIngredientNameTextView;
    CircleImageView addedIngredientImageView;
    ImageButton deleteIngredientButton;
    MainActivity.GlobalMode mode;
    EditText ingredientAmountEditText;

    public AddedIngredientListAdapter(List<Ingredient> addedIngredients, Context context, MainActivity.GlobalMode mode){
        this.context = context;
        this.addedIngredients = addedIngredients;
        this.mode = mode;
    }

    @NonNull
    @Override
    public AddedIngredientListAdapter.AddedIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.single_added_ingredient_item, parent, false);
        return new AddedIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedIngredientViewHolder holder, int position) {
        Ingredient addedIngredientListItem = addedIngredients.get(position);
        ingredientAmountEditText.setText(addedIngredientListItem.getAmount());
        addedIngredientNameTextView.setText(addedIngredientListItem.getName());
        Picasso.get().
                load(addedIngredientListItem.getImageUrl()).
                placeholder(R.mipmap.ic_launcher).
                into(addedIngredientImageView);

        ingredientAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CharSequence amount = charSequence;
                addedIngredientListItem.setAmount(String.valueOf(amount));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        deleteIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve and cache the system's default "short" animation time.
                RecipeIngredientsStep.removeIngredient(holder.getAdapterPosition());
                view.setVisibility(View.GONE);
                view.animate().alpha(0f).setDuration(android.R.integer.config_shortAnimTime).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
            }
        });

        if(mode == MainActivity.GlobalMode.EDIT){
            deleteIngredientButton.setVisibility(View.VISIBLE);
        }
        else if(mode == MainActivity.GlobalMode.VIEW)
        {
            deleteIngredientButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return addedIngredients.size();
    }

    public class AddedIngredientViewHolder extends RecyclerView.ViewHolder {

        public AddedIngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            addedIngredientNameTextView = itemView.findViewById(R.id.addedIngredientNameTextView);
            addedIngredientImageView = itemView.findViewById(R.id.addedIngredientImageView);
            deleteIngredientButton = itemView.findViewById(R.id.deleteIngredientBtn);
            ingredientAmountEditText = itemView.findViewById(R.id.ingredientAmountEditText);
        }
    }
}
