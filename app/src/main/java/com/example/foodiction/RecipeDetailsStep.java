package com.example.foodiction;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class RecipeDetailsStep extends Fragment implements Step {
    TextView nameTextView;
    TextView descriptionTextView;
    InputMethodManager imgr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        return inflater.inflate(R.layout.fragment_recipe_details_step, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nameTextView = (TextView) getView().findViewById(R.id.details_step_recipe_name_textinput);
        descriptionTextView = (TextView) getView().findViewById(R.id.descriptionInputTextEditor);

        nameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                AddRecipeActivity.currentCreatedRecipe.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        descriptionTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                AddRecipeActivity.currentCreatedRecipe.setDescription(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameTextView.requestFocus();
//        Only if you want to set up the keyboard
//        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        // Any of the details is empty
        if (nameTextView.getText().toString().isEmpty() || descriptionTextView.getText().toString().isEmpty()){
            return new VerificationError("Enter all details!");
        }

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
//        Toast.makeText(this.getActivity(), error.getErrorMessage().toString(), Toast.LENGTH_SHORT).show();
    }
}