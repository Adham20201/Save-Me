package com.adham.saveme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class done extends AppCompatActivity {
    protected int _splashTime = 2000;

    String foodTitle, userID;


    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        foodTitle = getIntent().getStringExtra("title");
        userID = getIntent().getStringExtra("id");

        new Handler().postDelayed(new Runnable() { // from class: com.example.foodonation.done.1
            @Override // java.lang.Runnable
            public void run() {
                done.this.finish();
                Intent i = new Intent(done.this, chat.class);
                i.putExtra("title",foodTitle);
                i.putExtra("id",userID);
                done.this.startActivity(i);
            }
        }, this._splashTime);
    }
}