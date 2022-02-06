package com.example.foodiction;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Date;
import java.util.Locale;

public class RecipeDurationStep extends Fragment implements Step{
    Button timePickerButton;
    int hour;
    int minute;
    private String makingDuration = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(!AddRecipeActivity.currentCreatedRecipe.getMakingDuration().isEmpty()){
            makingDuration = AddRecipeActivity.currentCreatedRecipe.getMakingDuration();
        }
        Log.i("Foodiction", "Making duration: "+ AddRecipeActivity.currentCreatedRecipe.makingDuration);
        return inflater.inflate(R.layout.fragment_recipe_duration_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timePickerButton = getView().findViewById(R.id.time_picker_button);
        if(makingDuration != ""){
            String[] timeData = makingDuration.split("h");
            int hour = Integer.valueOf(timeData[0].trim());
            int minute = Integer.valueOf(timeData[1].trim());
            String formattedString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            timePickerButton.setText(formattedString);
            Log.i("Foodiction", "Duration loaded");
        }
        View.OnClickListener pickTime = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                        makingDuration = String.format(Locale.getDefault(), "%d"+"h"+" %02d", hour, minute);
                        timePickerButton.setText(formattedTime);
                        AddRecipeActivity.currentCreatedRecipe.setMakingDuration(makingDuration);
                    }
                };
                int timerStyle = AlertDialog.THEME_HOLO_DARK;
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timerStyle, onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Making duration time");
                timePickerDialog.show();
            }
        };
        timePickerButton.setOnClickListener(pickTime);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("saved_making_duration", makingDuration);
        AddRecipeActivity.currentCreatedRecipe.setMakingDuration(makingDuration);
    }
}