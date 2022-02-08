package com.example.foodiction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class RecipeInternetPageActivity extends AppCompatActivity {
    WebView browser;
    static ProgressBar progressBar;
    String internetURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_internet_page);

        internetURL = getIntent().getStringExtra("recipe_internet_url");
        Log.i("URL", internetURL);


        progressBar = (ProgressBar) findViewById(R.id.recipe_internet_progress_bar);
        progressBar.setVisibility(View.VISIBLE);


        browser= (WebView) findViewById(R.id.internet_page_web_view);
        browser.loadUrl(internetURL);

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}