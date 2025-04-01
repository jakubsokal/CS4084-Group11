package com.example.libraryapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.libraryapp.db.users.Users;



public class LoginPage extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private Users dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.enterpassword);
        loginButton = findViewById(R.id.loginbutton);
        dbHelper = new Users(this);

        loginButton.setOnClickListener(view -> handleLogin());
    }
    private void handleLogin() {
        //takes the string inputs the users enter as the email and password
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // makes sures fields arent empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both fields!", Toast.LENGTH_SHORT).show();
            return;
        }
        // checks if details inserted are valid for that email and password
        boolean isValid = dbHelper.verifyUser(email, password);
        if (!isValid) {
            Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }



}