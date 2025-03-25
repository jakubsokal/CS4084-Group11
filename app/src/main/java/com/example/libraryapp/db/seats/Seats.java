package com.example.libraryapp.db.seats;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Seats extends SQLiteOpenHelper {
    private static final String DB_NAME = "libraryapp.db";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "seats";

    private static final String COL_1 = "seat_id";

    private static final String COL_2 = "seat_num";

    private static final String COL_3 = "table_id";

    private static final String COL_4 = "status"; // 1 = taken, 0 = free

    public Seats(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String querySeats = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ " (\n" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                COL_2 + " INTEGER,\n" +
                COL_3 + " INTEGER,\n" +
                COL_4 + " INTEGER,\n" +
                "FOREIGN KEY("+ COL_3 +") REFERENCES tables(table_id))";

        db.execSQL(querySeats);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
