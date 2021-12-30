package com.example.foodiction;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class IngredientsListAdapter extends ArrayAdapter<Ingredient> {
    Context mContext;
    ArrayList<Ingredient> ingredients;
    public IngredientsListAdapter(Context context, ArrayList<Ingredient> ingredientArrayList){
        super(context, R.layout.single_ingredient_item, R.id.ingredientName, ingredientArrayList);
        this.mContext = context;
        this.ingredients = ingredientArrayList;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ingredient ingredient = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_ingredient_item, parent, false);
        }

        ImageView ingredientImage = convertView.findViewById(R.id.ingredientImage);
        Picasso.get().
                load(ingredients.get(position).getImageUrl()).
                placeholder(R.mipmap.ic_launcher_round).
                into(ingredientImage);
        return super.getView(position, convertView, parent);
    }
}
