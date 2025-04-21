package com.example.libraryapp.db.tables;

public class Tables {
    public static final String TABLE_NAME = "tables";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "table_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "room_id INTEGER," +
            "table_number INTEGER," +
            "seat_count INTEGER," +
            "FOREIGN KEY(room_id) REFERENCES rooms(room_id)" +
            ")";
}
