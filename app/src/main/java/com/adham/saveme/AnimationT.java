package com.adham.saveme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AnimationT extends AppCompatActivity {
    @SuppressLint("RestrictedApi")
    protected int _splashTime = PathInterpolatorCompat.MAX_NUM_POINTS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        new Handler().postDelayed(new Runnable() { // from class: com.example.foodonation.Animation.1
            @Override // java.lang.Runnable
            public void run() {
                AnimationT.this.finish();
                AnimationT.this.startActivity(new Intent(AnimationT.this, ControlActivity.class));
            }
        }, this._splashTime);
    }
}