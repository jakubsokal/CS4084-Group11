package com.example.libraryapp.db.floors;

public class Floors {
    public static final String TABLE_NAME = "floors";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "floor_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "floor_number INTEGER" +
            ")";
}
