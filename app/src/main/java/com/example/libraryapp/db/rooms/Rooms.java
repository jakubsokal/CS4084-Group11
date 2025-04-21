package com.example.libraryapp.db.rooms;

public class Rooms {
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS rooms (" +
            "room_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "room_name TEXT," +
            "floor_id INTEGER," +
            "FOREIGN KEY(floor_id) REFERENCES floors(floor_id))";
}
