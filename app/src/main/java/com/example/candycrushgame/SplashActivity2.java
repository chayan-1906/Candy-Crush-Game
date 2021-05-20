package com.example.candycrushgame;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity2 extends AppCompatActivity {

    Handler handler;
    private TextView txtViewLoading;
    static Typeface satisfy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtViewLoading = findViewById ( R.id.txtViewLoading );
        satisfy = Typeface.createFromAsset ( getAssets (), "fonts/satisfy_regular.ttf" );
        txtViewLoading.setTypeface ( satisfy );

        handler=new Handler ();
        handler.postDelayed( () -> {
            Intent intent = new Intent (SplashActivity2.this, MainActivity.class);
            startActivity(intent);
            finish();
        },6000);
    }
}