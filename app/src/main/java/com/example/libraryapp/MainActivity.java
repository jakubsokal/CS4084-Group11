package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Animation animation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        animation = new Animation();
        
        Button login = findViewById(R.id.login);
        login.setOnClickListener(click -> {
            animation.animateButtonTint(click);
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            startActivity(intent);
        });
    }
}