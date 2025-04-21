package com.example.libraryapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.libraryapp.db.DatabaseHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class AlertsFragment extends Fragment implements AlertAdapter.OnAlertClickListener {
    private RecyclerView alertsRecyclerView;
    private AlertAdapter adapter;
    private DatabaseHelper dbHelper;
    private ChipGroup filterChipGroup;
    private int userId;
    private List<AlertItem> allAlerts = new ArrayList<>();
    private static final String FILTER_ALL = "all";
    private static final String FILTER_UNREAD = "unread";
    private static final String FILTER_READ = "read";
    private String currentFilter = FILTER_ALL;

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
        filterChipGroup = view.findViewById(R.id.filterChipGroup);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        dbHelper = new DatabaseHelper(requireContext());
        adapter = new AlertAdapter(new ArrayList<>(), this);
        alertsRecyclerView.setAdapter(adapter);
        
        setupFilterChips();
        loadAlerts();
    }

    private void setupFilterChips() {
        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.allChip) {
                currentFilter = FILTER_ALL;
            } else if (checkedId == R.id.unreadChip) {
                currentFilter = FILTER_UNREAD;
            } else if (checkedId == R.id.readChip) {
                currentFilter = FILTER_READ;
            }
            applyFilter();
        });
    }

    private void loadAlerts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = dbHelper.getAllAlertsForUser(db, userId);
        
        Cursor cursor = db.rawQuery(query, null);
        allAlerts.clear();
        
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
                allAlerts.add(alert);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        applyFilter();
    }

    private void applyFilter() {
        List<AlertItem> filteredAlerts = new ArrayList<>();
        for (AlertItem alert : allAlerts) {
            if (currentFilter.equals(FILTER_ALL) ||
                (currentFilter.equals(FILTER_READ) && alert.isRead()) ||
                (currentFilter.equals(FILTER_UNREAD) && !alert.isRead())) {
                filteredAlerts.add(alert);
            }
        }
        adapter.updateAlerts(filteredAlerts);
    }

    @Override
    public void onAlertClick(AlertItem alert) {
        if (!alert.isRead()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.markAlertAsRead(db, alert.getAlertId());
            alert.setRead(true);
            applyFilter();
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