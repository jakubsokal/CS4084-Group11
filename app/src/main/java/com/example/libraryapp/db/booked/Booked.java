package com.example.libraryapp.db.booked;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.libraryapp.Booking;
import com.example.libraryapp.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Booked {

    private final SQLiteDatabase db;

    public Booked(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();

        String query = "SELECT " +
                "b.book_id, b.date, b.start_time, " +
                "b.floor_id AS floor, " +
                "b.room_id AS room, " +
                "b.seat_id AS seat, " +
                "b.table_id AS table_name " +
                "FROM booked b";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("book_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String time = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String floor = cursor.getString(cursor.getColumnIndexOrThrow("floor"));
                String table = cursor.getString(cursor.getColumnIndexOrThrow("table_name"));
                String seat = cursor.getString(cursor.getColumnIndexOrThrow("seat"));

                Booking booking = new Booking(id, date, time, floor, table, seat);
                bookingList.add(booking);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bookingList;
    }
}
