package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryapp.db.users.Users;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmail, registerPassword, registerConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerConfirmPassword = findViewById(R.id.register_confirm_password);
        Button registerSubmit = findViewById(R.id.register_submit);
        TextView loginNow = findViewById(R.id.login_now_link);
        Button backButton = findViewById(R.id.back);

        registerSubmit.setOnClickListener(v -> {
            String email = registerEmail.getText().toString().trim();
            String password = registerPassword.getText().toString().trim();
            String confirmPassword = registerConfirmPassword.getText().toString().trim();

            //makes sure all fields filled in
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this,"Make Sure All Fields Are Filled", Toast.LENGTH_SHORT).show();
                return;
            }

            //checks if passwords match
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Make Sure Passwords Match", Toast.LENGTH_SHORT).show();
                return;
            }

            Users db = new Users(this);
            boolean successful = db.registerUser(email, password);

            if (successful) {
                Toast.makeText(this, "Registration Complete", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Registration Failed. Please Retry", Toast.LENGTH_SHORT).show();
            }
        });

        loginNow.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginPage.class));
            finish();
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginPage.class));
            finish();
        });
    }
}






