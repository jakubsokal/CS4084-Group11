package com.example.libraryapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.libraryapp.db.DatabaseHelper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.regex.Pattern;

public class LoginPage extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private DatabaseHelper dbHelper;
    private Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Animation animation = new Animation();
        setContentView(R.layout.activity_login_page);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        dbHelper = new DatabaseHelper(this);
        backButton = findViewById(R.id.back);
        TextView register = findViewById(R.id.register);
        register.setOnClickListener(click -> {
            animation.animateHyperLink(register);
            startActivity(new Intent(this, RegisterActivity.class));
        });
        loginButton.setOnClickListener(view -> {
            animation.animateButtonTint(loginButton);
            handleLogin();
        });
        TextView forgotPassword = findViewById(R.id.forgot_password);
        animation.animateHyperLink(forgotPassword);
        forgotPassword.setOnClickListener(v -> {
            animation.animateHyperLink(forgotPassword);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ResetPasswordFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });
        backButton.setOnClickListener(v -> {
            animation.animateButtonTint(backButton);
            startActivity(new Intent(LoginPage.this, MainActivity.class));
            finish();
        });
    }
    private void handleLogin() {
        String email = emailEditText.getText().toString().trim().toLowerCase();
        String password = passwordEditText.getText().toString().trim();
        String emailRegex = "^[\\w.-]+@studentmail.ul.ie$";
        String lecturerEmailRegex = "^[\\w.-]+@ul.ie$";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Pattern.matches(emailRegex, email) && !Pattern.matches(lecturerEmailRegex, email)) {
            Toast.makeText(this, "Please enter a University Of Limerick Email!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isValid = dbHelper.verifyUser(email, password);

        if (!isValid) {
            Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int userId = dbHelper.getUserIdByEmail(email);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_id", userId);
        editor.apply();
        
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

}