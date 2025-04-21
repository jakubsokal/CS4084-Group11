package com.example.libraryapp.db.seats;

public class Seats {
    public static final String TABLE_NAME = "seats";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "seat_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "seat_num INTEGER," +
            "table_id INTEGER," +
            "status INTEGER," + // 1 = taken, 0 = free
            "FOREIGN KEY(table_id) REFERENCES tables(table_id)" +
            ")";
}
