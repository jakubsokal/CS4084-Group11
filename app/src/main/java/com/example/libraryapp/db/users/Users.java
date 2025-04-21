package com.example.libraryapp.db.users;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.libraryapp.db.DatabaseHelper;
import com.example.libraryapp.db.Encryption;

import java.util.ArrayList;
import java.util.List;

public class Users {

    private final SQLiteDatabase db;

    public Users(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    // Get all emails
    public List<String> getAllEmails() {
        List<String> emails = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT email FROM users", null);
        if (cursor.moveToFirst()) {
            do {
                emails.add(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return emails;
    }

    // Verify login credentials
    public boolean verifyUser(String email, String enteredPassword) {
        Cursor cursor = db.rawQuery(
                "SELECT password FROM users WHERE email = ?",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            String storedHash = cursor.getString(0);
            Encryption encryption = new Encryption();
            boolean result = encryption.verify(enteredPassword, storedHash);
            cursor.close();
            return result;
        }

        cursor.close();
        return false; // no such user or password mismatch
    }
}
