package com.example.foodiction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

public class RecipeInstructionsStep extends Fragment implements Step {

    static ArrayList<Instruction> instructions;
    static AddInstructionAdapter mAdapter;
    static RecyclerView mRecyclerView;
    static EditText addInstructionEditText;
    Button addInstructionBtn;
    static TextView noInstructionsTextView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AddRecipeActivity.currentCreatedRecipe.instructions.size() == 0){
            instructions = new ArrayList<>();
        }
//        else{
//            Log.i("Foodiction", "Load has called!");
//            instructions = AddRecipeActivity.currentCreatedRecipe.getInstructions();
//        }
    }

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
        mRecyclerView = getView().findViewById(R.id.instructions_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AddInstructionAdapter(getContext(), instructions, MainActivity.GlobalMode.EDIT);
        mRecyclerView.setAdapter(mAdapter);
        noInstructionsTextView = getView().findViewById(R.id.no_ingredients_textview);

        addInstructionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String instructionText = addInstructionEditText.getText().toString();
                if(!instructionText.isEmpty()) {
                    addInstructionEditText.getText().clear();
                    addInstruction(instructionText);
                }
                else
                {
                    Snackbar.make(view, "Description must not be empty!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(instructions.size() == 0)
        {
            return new VerificationError("Add at least 1 instruction");
        }
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    public static void addInstruction(String instructionText){

            Instruction newInstruction = new Instruction(instructionText);
            instructions.add(newInstruction);
            AddRecipeActivity.currentCreatedRecipe.addInstruction(newInstruction);
            mRecyclerView.setAdapter(mAdapter);
    }

    public static void removeInstruction(int position){
        instructions.remove(position);
        AddRecipeActivity.currentCreatedRecipe.removeInstruction(position);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        AddRecipeActivity.currentCreatedRecipe.setInstructions(instructions);
    }
}