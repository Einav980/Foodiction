package com.example.foodiction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManageCategoriesListAdapter extends  RecyclerView.Adapter<ManageCategoriesListAdapter.AddCategoryViewHolder> {
    Context context;
    TextView categoryNameTextView;
    ImageButton categoryDeleteBtn;
    ArrayList<Category> categories;


    public ManageCategoriesListAdapter(Context context, ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public AddCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.single_category_item, parent, false);
        return new AddCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddCategoryViewHolder holder, int position) {
        categoryNameTextView.setText(categories.get(holder.getAdapterPosition()).getName());
        categoryDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete the category?");
                builder.setTitle("Delete category");

                builder.setCancelable(false);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ManageCategoriesFragment.deleteCategory(categories.get(holder.getAdapterPosition()));
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
        return categories.size();
    }

    public class AddCategoryViewHolder extends RecyclerView.ViewHolder {

        public AddCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.manage_categories_single_category_name);
            categoryDeleteBtn = itemView.findViewById(R.id.manage_categories_single_category_delete_button);
        }
    }
}
