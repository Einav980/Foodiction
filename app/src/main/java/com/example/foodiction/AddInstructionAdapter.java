package com.example.foodiction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddInstructionAdapter extends  RecyclerView.Adapter<AddInstructionAdapter.AddInstructionViewHolder> {
    Context context;
    ArrayList<Instruction> instructions;
    TextView instructionStepTitle;
    TextView descriptionTextView;
    ImageButton deleteButton;
    MainActivity.GlobalMode mode;

    public AddInstructionAdapter(Context context, ArrayList<Instruction> instructions, MainActivity.GlobalMode mode){
        this.context = context;
        this.instructions = instructions;
        this.mode = mode;
    }

    @NonNull
    @Override
    public AddInstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.single_instruction_item, parent, false);
        return new AddInstructionAdapter.AddInstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddInstructionViewHolder holder, int position) {
        instructionStepTitle.setText("Step "+ (holder.getAdapterPosition() + 1));
        descriptionTextView.setText(instructions.get(holder.getAdapterPosition()).getDescription());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeInstructionsStep.removeInstruction(holder.getAdapterPosition());
            }
        });

        if(mode == MainActivity.GlobalMode.VIEW){
            deleteButton.setVisibility(View.GONE);
        }
        else {
            deleteButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public class AddInstructionViewHolder extends RecyclerView.ViewHolder {

        public AddInstructionViewHolder(@NonNull View itemView) {
            super(itemView);

            instructionStepTitle = itemView.findViewById(R.id.instruction_step_title);
            descriptionTextView = itemView.findViewById(R.id.instruction_description_textview);
            deleteButton = itemView.findViewById(R.id.instruction_delete_button);

        }
    }
}
