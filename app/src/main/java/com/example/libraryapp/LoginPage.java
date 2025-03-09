package com.example.libraryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.libraryapp.db.Encryption;
import com.example.libraryapp.db.users.Users;

public class LoginPage extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView registerLink;
    private Users dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        dbHelper = new Users(this);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.enterpassword);
        loginButton = findViewById(R.id.loginbutton);
        registerLink = findViewById(R.id.textView);

        loginButton.setOnClickListener(v -> attemptLogin());

        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, RegisterActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"password", "status"};
        String selection = "email = ?";
        String[] selectionArgs = {email};

        try (Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int statusIndex = cursor.getColumnIndex("status");
                int passwordIndex = cursor.getColumnIndex("password");
                
                if (statusIndex != -1 && passwordIndex != -1) {
                    int status = cursor.getInt(statusIndex);
                    String storedHash = cursor.getString(passwordIndex);
                    
                    if (status != 1) {
                        Toast.makeText(this, "Account is inactive", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Encryption encryption = new Encryption();
                    if (encryption.verify(password, storedHash)) {
                        Intent intent = new Intent(LoginPage.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}