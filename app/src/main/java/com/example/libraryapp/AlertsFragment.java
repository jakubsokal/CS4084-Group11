package com.example.libraryapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.libraryapp.db.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class AlertsFragment extends Fragment implements AlertAdapter.OnAlertClickListener {
    private RecyclerView alertsRecyclerView;
    private AlertAdapter adapter;
    private DatabaseHelper dbHelper;
    private Button clearReadAlertsButton;
    private int userId;

    public AlertsFragment() {
        super(R.layout.fragment_alerts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getInt("userId", -1);
            if (userId == -1) {
                Toast.makeText(requireContext(), "Error: User not found", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        
        alertsRecyclerView = view.findViewById(R.id.alertsRecyclerView);
        clearReadAlertsButton = view.findViewById(R.id.clearReadAlertsButton);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        dbHelper = new DatabaseHelper(requireContext());
        adapter = new AlertAdapter(new ArrayList<>(), this);
        alertsRecyclerView.setAdapter(adapter);
        
        clearReadAlertsButton.setOnClickListener(v -> clearReadAlerts());
        
        loadAlerts();
    }

    private void loadAlerts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = dbHelper.getAllAlertsForUser(db, userId);
        
        Cursor cursor = db.rawQuery(query, null);
        List<AlertItem> alerts = new ArrayList<>();
        
        if (cursor.moveToFirst()) {
            do {
                AlertItem alert = new AlertItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                    cursor.getString(cursor.getColumnIndexOrThrow("message")),
                    cursor.getString(cursor.getColumnIndexOrThrow("alertType")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("status")) == 1,
                    cursor.getString(cursor.getColumnIndexOrThrow("createdAt"))
                );
                alerts.add(alert);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        adapter.updateAlerts(alerts);
    }

    private void clearReadAlerts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedCount = dbHelper.deleteReadAlerts(db, userId);
        
        if (deletedCount > 0) {
            Toast.makeText(requireContext(), 
                "Cleared " + deletedCount + " read alerts", 
                Toast.LENGTH_SHORT).show();
            loadAlerts();
        } else {
            Toast.makeText(requireContext(), 
                "No read alerts to clear", 
                Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAlertClick(AlertItem alert) {
        if (!alert.isRead()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.markAlertAsRead(db, alert.getAlertId());
            alert.setRead(true);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
} 