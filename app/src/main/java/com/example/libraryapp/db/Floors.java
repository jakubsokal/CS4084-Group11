package com.example.libraryapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
public class Floors extends SQLiteOpenHelper {
    private static final String DB_NAME = "libraryapp.db";

    private static final String TABLE_NAME = "floors";

    private static final String COL_1 = "floor_id";

    private static final String COL_2 = "floor_name";

    private static final int DB_VERSION = 1;

    public Floors(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryUsers = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ " (\n" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                COL_2 + " TEXT)";

        db.execSQL(queryUsers);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}