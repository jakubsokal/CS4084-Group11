package com.example.libraryapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.libraryapp.db.alerts.Alerts;
import com.example.libraryapp.db.floors.Floors;
import com.example.libraryapp.db.rooms.Rooms;
import com.example.libraryapp.db.tables.Tables;
import com.example.libraryapp.db.seats.Seats;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "libraryapp.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE," +
                "password TEXT)");

        // Booked table
        db.execSQL("CREATE TABLE IF NOT EXISTS booked (" +
                "book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "seat_id INTEGER," +
                "table_id INTEGER," +
                "room_id INTEGER," +
                "floor_id INTEGER," +
                "date TEXT," +
                "start_time TEXT," +
                "FOREIGN KEY(user_id) REFERENCES users(user_id))");

        db.execSQL(Floors.CREATE_TABLE);
        db.execSQL(Rooms.CREATE_TABLE);
        db.execSQL(Tables.CREATE_TABLE);
        db.execSQL(Seats.CREATE_TABLE);
        String password = new com.example.libraryapp.db.Encryption().encrypt("tester123");
        db.execSQL("INSERT INTO users (email, password) VALUES (?, ?)",
                new Object[]{"tester@ul.ie", password});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS seats");
        db.execSQL("DROP TABLE IF EXISTS tables");
        db.execSQL("DROP TABLE IF EXISTS rooms");
        db.execSQL("DROP TABLE IF EXISTS floors");
        db.execSQL("DROP TABLE IF EXISTS alerts");
        db.execSQL("DROP TABLE IF EXISTS booked");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}
