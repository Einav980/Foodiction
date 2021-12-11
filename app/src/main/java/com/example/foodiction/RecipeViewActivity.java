package com.example.foodiction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transformation.ExpandableBehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeViewActivity extends AppCompatActivity {
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> collection;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        createGroupList();
        createCollection();
    }

    public void testFunc(View view) {
        Log.i("FOODICTION", "Snackbar");
    }

    private void createCollection(){
        String[] models = {"A", "B"};
        collection = new HashMap<String, List<String>>();
    }
    private void createGroupList(){
        groupList = new ArrayList<>();
        groupList.add("a");
        groupList.add("b");
        groupList.add("c");
    }

    private void loadChild(){

    }
}