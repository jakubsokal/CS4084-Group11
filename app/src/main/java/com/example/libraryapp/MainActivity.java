package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryapp.db.booked.Booked;
import com.example.libraryapp.db.users.Users;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Booked booked = new Booked(this);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(click -> {
            new Animation().animateButtonTint(click);
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            startActivity(intent);
        });
        Button register = findViewById(R.id.register);
        register.setOnClickListener(click -> {
            new Animation().animateButtonTint(click);
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Register clicked", Toast.LENGTH_SHORT).show();

        });
    }
}