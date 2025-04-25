package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.libraryapp.db.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText registerEmail, registerPassword, registerConfirmPassword;
    private Button registerSubmit, backButton;
    private TextView loginNow;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "RegisterActivity onCreate called");
        dbHelper = new DatabaseHelper(this);

        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerConfirmPassword = findViewById(R.id.register_confirm_password);
        registerSubmit = findViewById(R.id.register_submit);
        loginNow = findViewById(R.id.login_now_link);
        backButton = findViewById(R.id.back);

        registerSubmit.setOnClickListener(v -> {
            Log.d(TAG, "Register button clicked");
            handleRegistration();
        });

        loginNow.setOnClickListener(v -> {
            Log.d(TAG, "Login now link clicked");
            startActivity(new Intent(RegisterActivity.this, LoginPage.class));
            finish();
        });

        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            startActivity(new Intent(RegisterActivity.this, LoginPage.class));
            finish();
        });
    }

    private void handleRegistration() {
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();
        String confirmPassword = registerConfirmPassword.getText().toString().trim();

        Log.d(TAG, "Handling registration for email: " + email);

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Log.d(TAG, "Empty fields detected");
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Log.d(TAG, "Passwords do not match");
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.endsWith("@studentmail.ul.ie") && !email.endsWith("@ul.ie")) {
            Log.d(TAG, "Invalid email format");
            Toast.makeText(this, "Please use a valid UL email address", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = email.split("@")[0];
        Log.d(TAG, "Attempting to register user with name: " + name);
        
        boolean success = dbHelper.registerUser(email, password, name);
        Log.d(TAG, "Registration result: " + success);

        if (success) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginPage.class));
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Email might already be registered.", Toast.LENGTH_SHORT).show();
        }
    }
}






