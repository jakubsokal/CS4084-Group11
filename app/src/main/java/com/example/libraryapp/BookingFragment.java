package com.example.libraryapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.libraryapp.db.DatabaseHelper;
import android.preference.PreferenceManager;
import android.os.Handler;
import android.os.Looper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingFragment extends Fragment {
    private TextView dateTextView, timeTextView;
    private Spinner floorSpinner, tableSpinner, seatSpinner, durationSpinner;
    private Button bookButton;
    private Calendar selectedDate, selectedTime;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private Handler mainHandler;

    public BookingFragment() {
        super(R.layout.fragment_booking);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation animation = new Animation();

        dateTextView = view.findViewById(R.id.dateTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        floorSpinner = view.findViewById(R.id.floorSpinner);
        tableSpinner = view.findViewById(R.id.tableSpinner);
        seatSpinner = view.findViewById(R.id.seatSpinner);
        durationSpinner = view.findViewById(R.id.durationSpinner);
        bookButton = view.findViewById(R.id.bookButton);
        progressBar = view.findViewById(R.id.progressBar);

        selectedDate = Calendar.getInstance();
        selectedTime = Calendar.getInstance();
        dbHelper = new DatabaseHelper(requireContext());
        mainHandler = new Handler(Looper.getMainLooper());

        dateTextView.setOnClickListener(v -> showDatePicker());
        timeTextView.setOnClickListener(v -> showTimePicker());
        bookButton.setOnClickListener(v -> {
            bookButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00A950")));
            animation.animateButtonTint(bookButton);
            validateAndShowConfirmation();
        });

        setupSpinners();
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
            (view, year, month, dayOfMonth) -> {
                selectedDate.set(year, month, dayOfMonth);
                updateDateDisplay();
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
            (view, hourOfDay, minute) -> {
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);
                updateTimeDisplay();
            },
            selectedTime.get(Calendar.HOUR_OF_DAY),
            selectedTime.get(Calendar.MINUTE),
            true
        );
        timePickerDialog.show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
        dateTextView.setText(sdf.format(selectedDate.getTime()));
    }

    private void updateTimeDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        timeTextView.setText(sdf.format(selectedTime.getTime()));
    }

    private void setupSpinners() {
        String[] floors = {"Floor 1", "Floor 2", "Floor 3"};
        String[] tables = {"Table 1", "Table 2", "Table 3", "Table 4", "Table 5"};
        String[] seats = {"Seat 1", "Seat 2", "Seat 3", "Seat 4"};
        String[] durations = {"30 minutes", "1 hour", "1.5 hours", "2 hours"};

        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_spinner_item, floors);
        ArrayAdapter<String> tableAdapter = new ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_spinner_item, tables);
        ArrayAdapter<String> seatAdapter = new ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_spinner_item, seats);
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(requireContext(), 
                android.R.layout.simple_spinner_item, durations);

        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        floorSpinner.setAdapter(floorAdapter);
        tableSpinner.setAdapter(tableAdapter);
        seatSpinner.setAdapter(seatAdapter);
        durationSpinner.setAdapter(durationAdapter);
    }

    private void validateAndShowConfirmation() {
        if (!validateFields()) {
            return;
        }

        String floor = floorSpinner.getSelectedItem().toString();
        String table = tableSpinner.getSelectedItem().toString();
        String seat = seatSpinner.getSelectedItem().toString();
        String duration = durationSpinner.getSelectedItem().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.getTime());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTime.getTime());

        String message = "Confirm booking:\n\n" +
                "Floor: " + floor + "\n" +
                "Table: " + table + "\n" +
                "Seat: " + seat + "\n" +
                "Date: " + date + "\n" +
                "Time: " + time + "\n" +
                "Duration: " + duration;

        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Booking")
                .setMessage(message)
                .setPositiveButton("Confirm", (dialog, which) -> handleBooking())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private boolean validateFields() {
        if (dateTextView.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (timeTextView.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please select a time", Toast.LENGTH_SHORT).show();
            return false;
        }

        Calendar now = Calendar.getInstance();
        Calendar selectedDateTime = (Calendar) selectedDate.clone();
        selectedDateTime.set(Calendar.HOUR_OF_DAY, selectedTime.get(Calendar.HOUR_OF_DAY));
        selectedDateTime.set(Calendar.MINUTE, selectedTime.get(Calendar.MINUTE));
        selectedDateTime.set(Calendar.SECOND, 0);
        selectedDateTime.set(Calendar.MILLISECOND, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        if (selectedDateTime.compareTo(now) < 0) {
            Toast.makeText(requireContext(), "Please select a future time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void handleBooking() {
        showLoading(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            showError("Please log in to make a booking");
            showLoading(false);
            return;
        }

        String selectedDuration = durationSpinner.getSelectedItem().toString();
        int durationMinutes = getDurationInMinutes(selectedDuration);
        Calendar endTime = (Calendar) selectedTime.clone();
        endTime.add(Calendar.MINUTE, durationMinutes);

        SimpleDateFormat dbTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        String startTimeStr = dbTimeFormat.format(selectedTime.getTime());
        String endTimeStr = dbTimeFormat.format(endTime.getTime());
        String dateStr = dbDateFormat.format(selectedDate.getTime());

        int floorId = getFloorId(floorSpinner.getSelectedItem().toString());
        int roomId = getRoomId(tableSpinner.getSelectedItem().toString());
        int tableId = getTableId(tableSpinner.getSelectedItem().toString());
        int seatId = getSeatId(seatSpinner.getSelectedItem().toString());

        new Thread(() -> {
            try {
                if (!dbHelper.isSeatAvailable(seatId, dateStr, startTimeStr, endTimeStr)) {
                    mainHandler.post(() -> {
                        showError("This seat is already booked for the selected time slot");
                        showLoading(false);
                    });
                    return;
                }

                boolean success = dbHelper.createBooking(userId, floorId, roomId, tableId, seatId, dateStr, startTimeStr, endTimeStr);
                
                mainHandler.post(() -> {
                    if (success) {
                        Toast.makeText(requireContext(), "Booking confirmed!", Toast.LENGTH_SHORT).show();
                        clearForm();
                    } else {
                        showError("Failed to create booking. Please try again.");
                    }
                    showLoading(false);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    showError("An error occurred. Please try again.");
                    showLoading(false);
                });
            }
        }).start();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        bookButton.setEnabled(!show);
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void clearForm() {
        selectedDate = Calendar.getInstance();
        selectedTime = Calendar.getInstance();
        updateDateDisplay();
        updateTimeDisplay();
        floorSpinner.setSelection(0);
        tableSpinner.setSelection(0);
        seatSpinner.setSelection(0);
        durationSpinner.setSelection(0);
    }

    private int getDurationInMinutes(String duration) {
        switch (duration) {
            case "30 minutes":
                return 30;
            case "1 hour":
                return 60;
            case "1.5 hours":
                return 90;
            case "2 hours":
                return 120;
        }
        return 60;
    }

    private int getFloorId(String floorName) {
        try {
            return Integer.parseInt(floorName.substring(floorName.indexOf(" ") + 1));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private int getRoomId(String roomName) {
        try {
            return Integer.parseInt(roomName.substring(roomName.indexOf(" ") + 1));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private int getTableId(String tableName) {
        try {
            return Integer.parseInt(tableName.substring(tableName.indexOf(" ") + 1));
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private int getSeatId(String seatName) {
        try {
            return Integer.parseInt(seatName.substring(seatName.indexOf(" ") + 1));
        } catch (NumberFormatException e) {
            return 1;
        }
    }
} 