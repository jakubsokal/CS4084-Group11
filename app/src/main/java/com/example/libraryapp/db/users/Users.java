package com.example.libraryapp.db.users;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.libraryapp.db.Encryption;

public class Users extends SQLiteOpenHelper {
    private static final String DB_NAME = "libraryapp.db";

    private static final String TABLE_NAME = "users";

    private static final String COL_1 = "user_id";

    private static final String COL_2 = "email";

    private static final String COL_3 = "password";

    private static final String COL_4 = "permission"; // 0 = student 1 = admin

    private static final String COL_5 = "status"; // 1 = active 2 = inactive

    private static final String COL_6 = "created_on";

    private static final int DB_VERSION = 1;

    public Users(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase(); //Just for testing so DB has a user entry
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryUsers = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ " (\n" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                COL_2 + " TEXT NOT NULL,\n" +
                COL_3 + " TEXT NOT NULL,\n" +
                COL_4 + " INTEGER DEFAULT 0,\n" +
                COL_5 + " INTEGER DEFAULT 1,\n" +
                COL_6 + " TEXT DEFAULT CURRENT_DATE)";

        db.execSQL(queryUsers);

        //Insert statement for testing logging
        String password = new Encryption().encrypt("tester123");
        String insert = "INSERT INTO users("
                + COL_2 + ", "
                + COL_3 + ")" +
                "VALUES('tester@ul.ie', '" +  password + "' )";

        db.execSQL(insert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}