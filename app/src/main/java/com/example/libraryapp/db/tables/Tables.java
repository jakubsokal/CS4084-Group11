package com.example.libraryapp.db.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Tables extends SQLiteOpenHelper {
    private static final String DB_NAME = "libraryapp.db";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "tables";

    private static final String COL_1 = "table_id";

    private static final String COL_2 = "room_id";

    private static final String COL_3 = "table_number";

    private static final String COL_4 = "seat_count";

    public Tables(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryTables = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ " (\n" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                COL_2 + " INTEGER,\n" +
                COL_3 + " INTEGER,\n" +
                COL_4 + "INETGER,\n" +
                "FOREIGN KEY("+ COL_2 +") REFERENCES rooms(room_id))";

        db.execSQL(queryTables);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
