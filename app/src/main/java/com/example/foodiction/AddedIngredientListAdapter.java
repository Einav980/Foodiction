package com.example.foodiction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddedIngredientListAdapter extends ArrayAdapter<Ingredient> {

    Context context;
    ArrayList<Ingredient> addedIngredients;
    TextView addedIngredientNameTextView;
    CircleImageView addedIngredientImageView;
    public AddedIngredientListAdapter(Context context, ArrayList<Ingredient> addedIngredients){
        super(context, R.layout.single_added_ingredient_item, R.id.addedIngredientNameTextView, addedIngredients);
        this.context = context;
        this.addedIngredients = addedIngredients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_added_ingredient_item, parent, false);
        }

        addedIngredientNameTextView = convertView.findViewById(R.id.addedIngredientNameTextView);
        addedIngredientImageView = convertView.findViewById(R.id.addedIngredientImageView);

        addedIngredientNameTextView.setText(addedIngredients.get(position).getName());
        addedIngredientImageView.setImageResource(addedIngredients.get(position).getImageId());

        return super.getView(position, convertView, parent);
    }
}
