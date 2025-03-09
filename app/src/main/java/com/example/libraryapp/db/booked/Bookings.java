package com.example.libraryapp.db.booked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.libraryapp.db.users.Users;

/**
 * Database helper class for managing library seat bookings.
 * This class handles all booking-related database operations including:
 * - Creating and managing bookings
 * - Checking seat availability
 * - Managing booking status
 */
public class Bookings extends SQLiteOpenHelper {
    private static final String DB_NAME = "libraryapp.db";
    private static final String TABLE_NAME = "bookings";
    private static final String COL_1 = "booking_id";
    private static final String COL_2 = "user_id";
    private static final String COL_3 = "floor_number";
    private static final String COL_4 = "seat_id";
    private static final String COL_5 = "booking_date";
    private static final String COL_6 = "start_time";
    private static final String COL_7 = "end_time";
    private static final String COL_8 = "status";
    
    // Booking status constants
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_COMPLETED = 2;
    public static final int STATUS_CANCELLED = 3;
    
    private static final int DB_VERSION = 1;
    private Context context;

    public Bookings(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        // Initialize users table first since bookings depends on it
        new Users(context);
        // Ensure our table exists
        SQLiteDatabase db = getWritableDatabase();
        ensureBookingsTableExists(db);
    }

    private void ensureBookingsTableExists(SQLiteDatabase db) {
        String queryBookings = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                COL_2 + " INTEGER NOT NULL,\n" +
                COL_3 + " INTEGER NOT NULL,\n" +
                COL_4 + " TEXT NOT NULL,\n" +
                COL_5 + " TEXT NOT NULL,\n" +
                COL_6 + " TEXT NOT NULL,\n" +
                COL_7 + " TEXT NOT NULL,\n" +
                COL_8 + " INTEGER DEFAULT " + STATUS_ACTIVE + ",\n" +
                "FOREIGN KEY(" + COL_2 + ") REFERENCES users(user_id))";

        db.execSQL(queryBookings);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ensureBookingsTableExists(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Checks if a seat is available for booking during the specified time slot.
     *
     * @param floorNumber The floor number where the seat is located
     * @param seatId The unique identifier of the seat
     * @param date The date of the booking (format: YYYY-MM-DD)
     * @param startTime The start time of the booking (format: HH:mm)
     * @param endTime The end time of the booking (format: HH:mm)
     * @return true if the seat is available, false otherwise
     */
    public boolean isSeatAvailable(int floorNumber, String seatId, String date, String startTime, String endTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME +
                " WHERE floor_number = ? AND seat_id = ? AND booking_date = ? " +
                "AND status = " + STATUS_ACTIVE + " " +
                "AND ((start_time <= ? AND end_time > ?) OR (start_time < ? AND end_time >= ?))";
        
        String[] selectionArgs = {
            String.valueOf(floorNumber),
            seatId,
            date,
            startTime,
            startTime,
            endTime,
            endTime
        };

        try (Cursor cursor = db.rawQuery(query, selectionArgs)) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count == 0;
            }
            return true;
        }
    }

    /**
     * Creates a new booking in the database.
     *
     * @param userId The ID of the user making the booking
     * @param floorNumber The floor number where the seat is located
     * @param seatId The unique identifier of the seat
     * @param date The date of the booking (format: YYYY-MM-DD)
     * @param startTime The start time of the booking (format: HH:mm)
     * @param endTime The end time of the booking (format: HH:mm)
     * @return The ID of the new booking, or -1 if the creation failed
     */
    public long createBooking(int userId, int floorNumber, String seatId, 
                            String date, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COL_2, userId);
        values.put(COL_3, floorNumber);
        values.put(COL_4, seatId);
        values.put(COL_5, date);
        values.put(COL_6, startTime);
        values.put(COL_7, endTime);
        values.put(COL_8, STATUS_ACTIVE);

        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * Retrieves all active bookings for a specific user.
     *
     * @param userId The ID of the user
     * @return A Cursor containing the booking records
     */
    public Cursor getUserBookings(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE user_id = ? AND status = " + STATUS_ACTIVE + 
                " ORDER BY booking_date, start_time";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    /**
     * Cancels an active booking.
     *
     * @param bookingId The ID of the booking to cancel
     * @return true if the booking was successfully cancelled, false otherwise
     */
    public boolean cancelBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_8, STATUS_CANCELLED);

        return db.update(TABLE_NAME, values, 
                COL_1 + " = ? AND status = " + STATUS_ACTIVE, 
                new String[]{String.valueOf(bookingId)}) > 0;
    }

    /**
     * Retrieves all active bookings for a specific floor and date.
     *
     * @param floorNumber The floor number to query
     * @param date The date to query (format: YYYY-MM-DD)
     * @return A Cursor containing the booking records
     */
    public Cursor getFloorBookings(int floorNumber, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE floor_number = ? AND booking_date = ? AND status = " + STATUS_ACTIVE + 
                " ORDER BY start_time";
        return db.rawQuery(query, new String[]{String.valueOf(floorNumber), date});
    }
} 