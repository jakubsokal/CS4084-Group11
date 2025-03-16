package com.example.libraryapp.db.alerts;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class Alerts extends SQLiteOpenHelper {
    private static final String DB_NAME = "libraryapp.db";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "alerts";

    private static final String COL_1 = "alertId";
    private static final String COL_2 = "userId";
    private static final String COL_3 = "message";
    private static final String COL_4 = "alertType";
    private static final String COL_5 = "status";
    private static final String COL_6 = "createdAt";

    public Alerts(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryAlert = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                COL_2 + " INTEGER NOT NULL,\n" +
                COL_3 + " TEXT NOT NULL,\n" +
                COL_4 + " VARCHAR(50) NOT NULL,\n" +
                COL_5 + " INTEGER DEFAULT 0,\n" +
                COL_6 + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                "FOREIGN KEY(" + COL_2 + ") REFERENCES users(user_id))";

        db.execSQL(queryAlert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Helper method to create a new alert
    public long createAlert(SQLiteDatabase db, int userId, String message, String alertType) {
        ContentValues values = new ContentValues();
        values.put(COL_2, userId);
        values.put(COL_3, message);
        values.put(COL_4, alertType);
        
        return db.insert(TABLE_NAME, null, values);
    }

    // Helper method to mark an alert as read
    public void markAlertAsRead(SQLiteDatabase db, int alertId) {
        ContentValues values = new ContentValues();
        values.put(COL_5, 1);
        db.update(TABLE_NAME, values, COL_1 + " = ?", new String[]{String.valueOf(alertId)});
    }

    // Helper method to get all alerts for a user
    public String getAllAlertsForUser(SQLiteDatabase db, int userId) {
        return "SELECT * FROM " + TABLE_NAME + 
            " WHERE " + COL_2 + " = " + userId + 
            " ORDER BY " + COL_6 + " DESC";
    }

    // Helper method to get unread alerts count for a user
    public String getUnreadAlertsCountForUser(SQLiteDatabase db, int userId) {
        return "SELECT COUNT(*) FROM " + TABLE_NAME + 
            " WHERE " + COL_2 + " = " + userId + 
            " AND " + COL_5 + " = 0";
    }

    // Helper method to delete read alerts for a user
    public int deleteReadAlerts(SQLiteDatabase db, int userId) {
        String whereClause = COL_2 + " = ? AND " + COL_5 + " = 1";
        String[] whereArgs = {String.valueOf(userId)};
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }
}
