package com.example.foodiction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class OpenLogoActivity extends AppCompatActivity {
    private final int SPLASH_DELAY = 2500;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_logo);
        getWindow().setBackgroundDrawable(null);
        initializeView();
        animateLogo();
        goToMainActivity();
    }

    private void initializeView() {
        imageView = findViewById(R.id.imageView);

    }

    private void animateLogo() {
        Animation fadingInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadingInAnimation.setDuration(SPLASH_DELAY);
        imageView.startAnimation(fadingInAnimation);
    }

    private void goToMainActivity() {

        new Handler().postDelayed(()-> {
            startActivity(new Intent(OpenLogoActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, SPLASH_DELAY);
    }
}