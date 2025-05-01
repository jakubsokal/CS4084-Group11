package com.example.libraryapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.libraryapp.Booking;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 5;
    private static final String TAG = "DatabaseHelper";
    private final Encryption encryption;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String TABLE_ALERTS = "alerts";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_FLOOR = "floor";
    private static final String COLUMN_TABLE = "tableNumber";
    private static final String COLUMN_SEAT = "seatNumber";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_START_TIME = "startTime";
    private static final String COLUMN_END_TIME = "endTime";
    private static final String COLUMN_IS_CANCELLED = "isCancelled";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_ALERT_TYPE = "alertType";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_CREATED_AT = "createdAt";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHelper constructor called with version: " + DATABASE_VERSION);
        encryption = new Encryption();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate called - creating database tables");
        try {
            Log.d(TAG, "Creating users table...");
            db.execSQL("CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "email TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "name TEXT NOT NULL)");
            Log.d(TAG, "Users table created successfully");

            Log.d(TAG, "Creating bookings table...");
            db.execSQL("CREATE TABLE bookings (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId INTEGER NOT NULL," +
                    "floor INTEGER NOT NULL," +
                    "tableNumber INTEGER NOT NULL," +
                    "seatNumber INTEGER NOT NULL," +
                    "date TEXT NOT NULL," +
                    "startTime TEXT NOT NULL," +
                    "endTime TEXT NOT NULL," +
                    "isCancelled INTEGER DEFAULT 0," +
                    "FOREIGN KEY(userId) REFERENCES users(id))");
            Log.d(TAG, "Bookings table created successfully");

            Log.d(TAG, "Creating alerts table...");
            db.execSQL("CREATE TABLE alerts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userId INTEGER NOT NULL," +
                    "message TEXT NOT NULL," +
                    "alertType TEXT NOT NULL," +
                    "status INTEGER DEFAULT 0," +
                    "createdAt TEXT NOT NULL," +
                    "FOREIGN KEY(userId) REFERENCES users(id))");
            Log.d(TAG, "Alerts table created successfully");

            Log.d(TAG, "All tables created successfully, inserting test data...");
            
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_EMAIL, "tester@ul.ie");
            userValues.put(COLUMN_PASSWORD, encryption.encrypt("tester123"));
            userValues.put(COLUMN_NAME, "Test User");
            long userId = db.insert(TABLE_USERS, null, userValues);
            Log.d(TAG, "Inserted test user with ID: " + userId);

            ContentValues bookingValues = new ContentValues();
            bookingValues.put(COLUMN_USER_ID, userId);
            bookingValues.put(COLUMN_FLOOR, 1);
            bookingValues.put(COLUMN_TABLE, 1);
            bookingValues.put(COLUMN_SEAT, 1);
            bookingValues.put(COLUMN_DATE, "2023-05-01");
            bookingValues.put(COLUMN_START_TIME, "09:00");
            bookingValues.put(COLUMN_END_TIME, "10:00");
            bookingValues.put(COLUMN_IS_CANCELLED, 0);
            long bookingId = db.insert(TABLE_BOOKINGS, null, bookingValues);
            Log.d(TAG, "Inserted test booking with ID: " + bookingId);

            Log.d(TAG, "Database initialization completed");
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating database tables: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade called from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS alerts");
        db.execSQL("DROP TABLE IF EXISTS bookings");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean verifyUser(String email, String password) {
        Log.d(TAG, "Verifying user: " + email);
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String[] columns = {COLUMN_ID, COLUMN_PASSWORD};
            String selection = COLUMN_EMAIL + " = ?";
            String[] selectionArgs = {email};
            
            Log.d(TAG, "Querying users table with email: " + email);
            Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            
            boolean exists = false;
            if (cursor.moveToFirst()) {
                String storedHash = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                exists = encryption.verify(password, storedHash);
                Log.d(TAG, "Password verification result: " + exists);
            }
            
            Log.d(TAG, "User verification result: " + exists + ", found " + cursor.getCount() + " matching users");
            cursor.close();
            return exists;
        } catch (Exception e) {
            Log.e(TAG, "Error verifying user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        return userId;
    }

    public boolean registerUser(String email, String password, String name) {
        Log.d(TAG, "registerUser called for email: " + email);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String[] columns = {COLUMN_ID};
            String selection = COLUMN_EMAIL + " = ?";
            String[] selectionArgs = {email};
            Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            
            if (cursor.getCount() > 0) {
                Log.d(TAG, "Email already exists: " + email);
                cursor.close();
                return false;
            }
            cursor.close();
            Log.d(TAG, "Email is available, proceeding with registration");

            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, email);
            String encryptedPassword = encryption.encrypt(password);
            Log.d(TAG, "Password encrypted successfully");
            values.put(COLUMN_PASSWORD, encryptedPassword);
            values.put(COLUMN_NAME, name);

            long result = db.insert(TABLE_USERS, null, values);
            Log.d(TAG, "User registration result: " + (result != -1 ? "success" : "failed"));
            return result != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getAllAlertsForUser(SQLiteDatabase db, int userId) {
        return "SELECT * FROM " + TABLE_ALERTS + " WHERE " + COLUMN_USER_ID + " = " + userId + " ORDER BY " + COLUMN_CREATED_AT + " DESC";
    }

    public int deleteReadAlerts(SQLiteDatabase db, int userId) {
        return db.delete(TABLE_ALERTS, COLUMN_USER_ID + " = ? AND " + COLUMN_STATUS + " = 1", new String[]{String.valueOf(userId)});
    }

    public void markAlertAsRead(SQLiteDatabase db, int alertId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, 1);
        db.update(TABLE_ALERTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(alertId)});
    }

    public void createBookingAlert(SQLiteDatabase db, int userId, int bookingId, String alertType) {
        String message;
        String bookingDate = "";
        
        Cursor cursor = db.query(TABLE_BOOKINGS, new String[]{COLUMN_DATE}, COLUMN_ID + " = ?", 
            new String[]{String.valueOf(bookingId)}, null, null, null);
        if (cursor.moveToFirst()) {
            bookingDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
        }
        cursor.close();

        if (alertType.equals("BOOKING_CONFIRMED")) {
            message = "Your booking #" + bookingId + " for " + bookingDate + " has been confirmed.";
        } else if (alertType.equals("BOOKING_CANCELLED")) {
            message = "Your booking #" + bookingId + " for " + bookingDate + " has been cancelled.";
        } else {
            message = "An update regarding your booking #" + bookingId + " for " + bookingDate;
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_MESSAGE, message);
        values.put(COLUMN_ALERT_TYPE, alertType);
        values.put(COLUMN_STATUS, 0);
        values.put(COLUMN_CREATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        db.insert(TABLE_ALERTS, null, values);
    }

    public List<Booking> getBookingsForUser(int userId) {
        List<Booking> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_BOOKINGS + 
                " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_IS_CANCELLED + " = 0 " +
                " ORDER BY " + COLUMN_DATE + " DESC, " + COLUMN_START_TIME + " DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Booking booking = new Booking(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FLOOR)),
                    0,
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEAT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TABLE)),
                    "Floor " + cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FLOOR)),
                    "Room 1",
                    "Seat " + cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEAT)),
                    "Table " + cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TABLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_TIME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CANCELLED)) == 1,
                    60
                );
                bookings.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookings;
    }

    public boolean cancelBooking(int bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_CANCELLED, 1);
        int rowsAffected = db.update(TABLE_BOOKINGS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(bookingId)});
        
        if (rowsAffected > 0) {
            Cursor cursor = db.query(TABLE_BOOKINGS, new String[]{COLUMN_USER_ID}, COLUMN_ID + " = ?", new String[]{String.valueOf(bookingId)}, null, null, null);
            if (cursor.moveToFirst()) {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                createBookingAlert(db, userId, bookingId, "BOOKING_CANCELLED");
            }
            cursor.close();
        }
        
        return rowsAffected > 0;
    }

    public boolean isSeatAvailable(int seatId, String date, String startTime, String endTime) {
        return isSeatAvailable(1, 1, seatId, date, startTime, endTime);
    }

    public boolean isSeatAvailable(int floor, int table, int seat, String date, String startTime, String endTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_BOOKINGS + " WHERE " +
                COLUMN_FLOOR + " = ? AND " +
                COLUMN_TABLE + " = ? AND " +
                COLUMN_SEAT + " = ? AND " +
                COLUMN_DATE + " = ? AND " +
                COLUMN_IS_CANCELLED + " = 0 AND " +
                "((? BETWEEN " + COLUMN_START_TIME + " AND " + COLUMN_END_TIME + ") OR " +
                "(? BETWEEN " + COLUMN_START_TIME + " AND " + COLUMN_END_TIME + "))";

        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(floor),
                String.valueOf(table),
                String.valueOf(seat),
                date,
                startTime,
                endTime
        });
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    public boolean isSeatAvailableExcludingBooking(int floor, int table, int seat, String date, String startTime, String endTime, int excludeBookingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_BOOKINGS + " WHERE " +
                COLUMN_FLOOR + " = ? AND " +
                COLUMN_TABLE + " = ? AND " +
                COLUMN_SEAT + " = ? AND " +
                COLUMN_DATE + " = ? AND " +
                COLUMN_IS_CANCELLED + " = 0 AND " +
                COLUMN_ID + " != ? AND " +
                "((? BETWEEN " + COLUMN_START_TIME + " AND " + COLUMN_END_TIME + ") OR " +
                "(? BETWEEN " + COLUMN_START_TIME + " AND " + COLUMN_END_TIME + "))";

        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(floor),
                String.valueOf(table),
                String.valueOf(seat),
                date,
                String.valueOf(excludeBookingId),
                startTime,
                endTime
        });
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    public boolean editBooking(int bookingId, int floor, int table, int seat, String date, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_BOOKINGS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookingId)}, null, null, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }

        int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        cursor.close();

        if (!isSeatAvailableExcludingBooking(floor, table, seat, date, startTime, endTime, bookingId)) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_FLOOR, floor);
        values.put(COLUMN_TABLE, table);
        values.put(COLUMN_SEAT, seat);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_END_TIME, endTime);

        int rowsAffected = db.update(TABLE_BOOKINGS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(bookingId)});

        if (rowsAffected > 0) {
            createBookingAlert(db, userId, bookingId, "BOOKING_UPDATED");
            return true;
        }
        return false;
    }

    public boolean createBooking(int userId, int floorId, int roomId, int tableId, int seatId, String date, String startTime, String endTime) {
        return createBooking(userId, floorId, tableId, seatId, date, startTime, endTime);
    }

    public boolean createBooking(int userId, int floor, int table, int seat, String date, String startTime, String endTime) {
        if (!isSeatAvailable(floor, table, seat, date, startTime, endTime)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_FLOOR, floor);
        values.put(COLUMN_TABLE, table);
        values.put(COLUMN_SEAT, seat);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_END_TIME, endTime);
        values.put(COLUMN_IS_CANCELLED, 0);

        long bookingId = db.insert(TABLE_BOOKINGS, null, values);
        if (bookingId != -1) {
            createBookingAlert(db, userId, (int) bookingId, "BOOKING_CONFIRMED");
            return true;
        }
        return false;
    }
} 