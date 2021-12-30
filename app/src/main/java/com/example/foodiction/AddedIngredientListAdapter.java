package com.example.foodiction;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddedIngredientListAdapter extends ArrayAdapter<Ingredient> {

    Context context;
    ArrayList<Ingredient> addedIngredients;
    TextView addedIngredientNameTextView;
    CircleImageView addedIngredientImageView;
    ImageButton deleteIngredientButton;
    MainActivity.GlobalMode mode;
    EditText ingredientAmountEditText;

    public AddedIngredientListAdapter(Context context, ArrayList<Ingredient> addedIngredients, MainActivity.GlobalMode mode){
        super(context, R.layout.single_added_ingredient_item, R.id.addedIngredientNameTextView, addedIngredients);

        this.context = context;
        this.addedIngredients = addedIngredients;
        this.mode = mode;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_added_ingredient_item, parent, false);
        }

        addedIngredientNameTextView = convertView.findViewById(R.id.addedIngredientNameTextView);
        addedIngredientImageView = convertView.findViewById(R.id.addedIngredientImageView);
        deleteIngredientButton = convertView.findViewById(R.id.deleteIngredientBtn);
        ingredientAmountEditText = convertView.findViewById(R.id.ingredientAmountEditText);
        ingredientAmountEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    String amount = ingredientAmountEditText.getText().toString();
                    if(!amount.isEmpty()){
                        addedIngredients.get(position).setAmount(amount);
                    }
                }
            }
        });

        ingredientAmountEditText.setText(addedIngredients.get(position).getAmount());

        addedIngredientNameTextView.setText(addedIngredients.get(position).getName());
        Picasso.get().
                load(addedIngredients.get(position).getImageUrl()).
                placeholder(R.mipmap.ic_launcher).
                into(addedIngredientImageView);

        deleteIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeIngredientsStep.removeIngredient(position);
            }
        });

        if(mode == MainActivity.GlobalMode.EDIT){
            deleteIngredientButton.setVisibility(View.VISIBLE);
        }
        else if(mode == MainActivity.GlobalMode.VIEW)
        {
            deleteIngredientButton.setVisibility(View.INVISIBLE);
        }

        return super.getView(position, convertView, parent);
    }
}
