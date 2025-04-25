package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Animation animation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        animation = new Animation();
        
        Button login = findViewById(R.id.login);
        login.setOnClickListener(click -> {
            Log.d(TAG, "Login button clicked");
            animation.animateButtonTint(click);
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            startActivity(intent);
        });
        
        Button register = findViewById(R.id.register);
        register.setOnClickListener(click -> {
            Log.d(TAG, "Register button clicked");
            animation.animateButtonTint(click);
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}