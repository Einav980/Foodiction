package com.example.foodiction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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
    TextView ingredientAmountTextView;

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
        ingredientAmountTextView.setText(addedIngredientListItem.getAmount());
        addedIngredientNameTextView.setText(addedIngredientListItem.getDisplayName());
        Picasso.get().
                load(addedIngredientListItem.getImageUrl()).
                placeholder(R.drawable.ic_food_placeholder).
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
            ingredientAmountTextView.setVisibility(View.INVISIBLE);
            ingredientAmountEditText.setVisibility(View.VISIBLE);
        }
        else if(mode == MainActivity.GlobalMode.VIEW)
        {
            deleteIngredientButton.setVisibility(View.INVISIBLE);
            ingredientAmountTextView.setVisibility(View.VISIBLE);
            ingredientAmountEditText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return addedIngredients.size();
    }

    public class AddedIngredientViewHolder extends RecyclerView.ViewHolder {

        public AddedIngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            addedIngredientNameTextView = itemView.findViewById(R.id.added_ingredient_name_textview);
            addedIngredientImageView = itemView.findViewById(R.id.added_ingredient_imageview);
            deleteIngredientButton = itemView.findViewById(R.id.delete_ingredient_button);
            ingredientAmountEditText = itemView.findViewById(R.id.ingredient_amount_edit_text);
            ingredientAmountTextView = itemView.findViewById(R.id.ingredient_amount_text_view);
        }
    }
}
