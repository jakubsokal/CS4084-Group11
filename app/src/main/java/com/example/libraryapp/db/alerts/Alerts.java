package com.example.libraryapp.db.alerts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Alerts extends SQLiteOpenHelper {
    private static final String DB_NAME = "libraryapp.db";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "booked";

    private static final String COL_1 = "alert_id";

    private static final String COL_2 = "room_id";

    private static final String COL_3 = "seat_id";

    private static final String COL_4 = "date";

    private static final String COL_5 = "created_on";

    public Alerts(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryAlert = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ " (\n" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                COL_2 + " INTEGER NOT NULL,\n" +
                COL_3 + " INTEGER NOT NULL,\n" +
                COL_4 + " TEXT NOT NULL,\n" +
                COL_5 + " TEXT DEFAULT CURRENT_DATE)";

        db.execSQL(queryAlert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
