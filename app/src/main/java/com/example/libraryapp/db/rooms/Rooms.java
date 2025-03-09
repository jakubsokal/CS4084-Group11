package com.example.libraryapp.db.rooms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Rooms extends SQLiteOpenHelper {

    private static final String DB_NAME = "libraryapp.db";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "rooms";

    private static final String COL_1 = "room_id";

    private static final String COL_2 = "room_name";

    private static final String COL_3 = "floor_id";

    public Rooms(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryRooms = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ " (\n" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                COL_2 + " TEXT,\n" +
                COL_3 + " INTEGER,\n" +
                "FOREIGN KEY("+ COL_3 +") REFERENCES floors(floor_id))";

        db.execSQL(queryRooms);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
