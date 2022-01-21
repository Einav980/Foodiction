package com.example.foodiction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

public class RecipeInstructionsStep extends Fragment implements Step {

    static ArrayList<Instruction> instructions;
    static AddInstructionAdapter adapter;
    static ListView instructionsListView;
    static EditText addInstructionEditText;
    Button addInstructionBtn;

    static TextView noInstructionsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_instructions_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        addInstructionEditText = getView().findViewById(R.id.instruction_description_edittext);
        addInstructionBtn = getView().findViewById(R.id.add_instruction_button);
        instructions = new ArrayList<>();
        instructionsListView = getView().findViewById(R.id.instructions_listview);
        adapter = new AddInstructionAdapter(getContext(), instructions);
        instructionsListView.setAdapter(adapter);
        noInstructionsTextView = getView().findViewById(R.id.no_ingredients_textview);

        addInstructionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addInstruction(view);
            }
        });
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(instructions.size() == 0)
        {
            return new VerificationError("At least 1 instruction");
        }
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    public static void addInstruction(View view){
        String instructionText = addInstructionEditText.getText().toString();
        if(!instructionText.isEmpty()) {
            addInstructionEditText.setText("");
            instructions.add(new Instruction(instructionText));
            instructionsListView.setAdapter(adapter);
        }
        else
        {
            Snackbar.make(view, "Description must not be empty!", Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void removeInstruction(int position){
        instructions.remove(position);
        instructionsListView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("IS_EDITING", true);
    }
}