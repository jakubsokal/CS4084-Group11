package com.example.libraryapp.db.booked;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Booked extends SQLiteOpenHelper {
    private static final String DB_NAME = "libraryapp.db";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "booked";

    private static final String COL_1 = "book_id";

    private static final String COL_2 = "user_id";

    private static final String COL_3 = "floor_id";

    private static final String COL_4 = "room_id";

    private static final String COL_5 = "seat_id";

    private static final String COL_6 = "table_id";

    private static final String COL_7 = "date";

    private static final String COL_8 = "start_time";

    private static final String COL_9 = "end_time";

    private static final String COL_10 = "status"; // 1 = cancelled, 0 = booked

    private static final String COL_11 = "created_on";

    public Booked(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryBook = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ " (\n" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                COL_2 + " INTEGER, \n" +
                COL_3 + " INTEGER,\n" +
                COL_4 + " INTEGER,\n" +
                COL_5 + " INTEGER,\n" +
                COL_6 + " INTEGER," +
                COL_7 + " TEXT,\n" +
                COL_8 + " TEXT,\n" +
                COL_9 + " TEXT,\n" +
                COL_10 + " INTEGER,\n" +
                COL_11 + " TEXT DEFAULT CURRENT_DATE,\n" +
                "FOREIGN KEY("+ COL_2 +") REFERENCES users(user_id),\n"+
                "FOREIGN KEY("+ COL_3 +") REFERENCES floors(floor_id),\n" +
                "FOREIGN KEY("+ COL_4 +") REFERENCES rooms(room_id),\n" +
                "FOREIGN KEY("+ COL_5 +") REFERENCES seats(seat_id),\n" +
                "FOREIGN KEY("+ COL_6 +") REFERENCES tables(table_id))";

        db.execSQL(queryBook);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
