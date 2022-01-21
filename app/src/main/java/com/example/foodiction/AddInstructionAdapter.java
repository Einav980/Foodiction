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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddInstructionAdapter extends ArrayAdapter<Instruction> {
    Context context;
    ArrayList<Instruction> instructions;
    TextView instructionStepTitle;
    TextView descriptionTextView;
    ImageButton deleteButton;

    public AddInstructionAdapter(Context context, ArrayList<Instruction> instructions){
        super(context, R.layout.single_instruction_item, R.id.instruction_description_textview, instructions);
        this.context = context;
        this.instructions = instructions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_instruction_item, parent, false);
        }
        instructionStepTitle = convertView.findViewById(R.id.instruction_step_title);
        descriptionTextView = convertView.findViewById(R.id.instruction_description_textview);
        deleteButton = convertView.findViewById(R.id.instruction_delete_button);

        instructionStepTitle.setText("Step "+ (position + 1));
        descriptionTextView.setText(instructions.get(position).getDescription());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeInstructionsStep.removeInstruction(position);
            }
        });


        return super.getView(position, convertView, parent);
    }
}
