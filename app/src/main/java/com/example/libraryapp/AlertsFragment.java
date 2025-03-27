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
import com.example.libraryapp.db.alerts.Alerts;
import java.util.ArrayList;
import java.util.List;

public class AlertsFragment extends Fragment implements AlertAdapter.OnAlertClickListener {
    private RecyclerView alertsRecyclerView;
    private AlertAdapter adapter;
    private Alerts alertsDb;
    private Button clearReadAlertsButton;
    // TODO: Get the actual user ID of user using the app
    private final int userId = 1; // Temp UserID

    public AlertsFragment() {
        super(R.layout.fragment_alerts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        alertsRecyclerView = view.findViewById(R.id.alertsRecyclerView);
        clearReadAlertsButton = view.findViewById(R.id.clearReadAlertsButton);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        alertsDb = new Alerts(requireContext());
        adapter = new AlertAdapter(new ArrayList<>(), this);
        alertsRecyclerView.setAdapter(adapter);
        
        clearReadAlertsButton.setOnClickListener(v -> clearReadAlerts());
        
        loadAlerts();
    }

    private void loadAlerts() {
        SQLiteDatabase db = alertsDb.getReadableDatabase();
        String query = alertsDb.getAllAlertsForUser(db, userId);
        
        Cursor cursor = db.rawQuery(query, null);
        List<AlertItem> alerts = new ArrayList<>();
        
        if (cursor.moveToFirst()) {
            do {
                AlertItem alert = new AlertItem(
                    cursor.getInt(cursor.getColumnIndexOrThrow("alertId")),
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
        SQLiteDatabase db = alertsDb.getWritableDatabase();
        int deletedCount = alertsDb.deleteReadAlerts(db, userId);
        
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
            SQLiteDatabase db = alertsDb.getWritableDatabase();
            alertsDb.markAlertAsRead(db, alert.getAlertId());
            alert.setRead(true);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertsDb != null) {
            alertsDb.close();
        }
    }
} 