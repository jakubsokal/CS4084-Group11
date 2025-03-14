package com.example.libraryapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingFragment extends Fragment {
    private TextView dateTextView, timeTextView;
    private Spinner floorSpinner, tableSpinner, seatSpinner;
    private Button bookButton;
    private Calendar selectedDate, selectedTime;

    public BookingFragment() {
        super(R.layout.fragment_booking);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        dateTextView = view.findViewById(R.id.dateTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        floorSpinner = view.findViewById(R.id.floorSpinner);
        tableSpinner = view.findViewById(R.id.tableSpinner);
        seatSpinner = view.findViewById(R.id.seatSpinner);
        bookButton = view.findViewById(R.id.bookButton);

        selectedDate = Calendar.getInstance();
        selectedTime = Calendar.getInstance();

        dateTextView.setOnClickListener(v -> showDatePicker());
        timeTextView.setOnClickListener(v -> showTimePicker());
        setupSpinners();
        bookButton.setOnClickListener(v -> handleBooking());
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        dateTextView.setText(dateFormat.format(selectedDate.getTime()));
    }

    private void updateTimeDisplay() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        timeTextView.setText(timeFormat.format(selectedTime.getTime()));
    }

    private void setupSpinners() {
        String[] floors = {"Ground Floor", "First Floor", "Second Floor", "Third Floor"};
        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(requireContext(),
            android.R.layout.simple_spinner_item, floors);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(floorAdapter);

        String[] tables = {"Table 1", "Table 2", "Table 3", "Table 4", "Table 5"};
        ArrayAdapter<String> tableAdapter = new ArrayAdapter<>(requireContext(),
            android.R.layout.simple_spinner_item, tables);
        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSpinner.setAdapter(tableAdapter);

        String[] seats = {"Seat 1", "Seat 2", "Seat 3", "Seat 4"};
        ArrayAdapter<String> seatAdapter = new ArrayAdapter<>(requireContext(),
            android.R.layout.simple_spinner_item, seats);
        seatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatSpinner.setAdapter(seatAdapter);
    }

    private void handleBooking() {
        String bookingDetails = String.format("Booking confirmed for %s at %s, %s, %s, %s",
            dateTextView.getText(),
            timeTextView.getText(),
            floorSpinner.getSelectedItem(),
            tableSpinner.getSelectedItem(),
            seatSpinner.getSelectedItem()
        );
        Toast.makeText(requireContext(), bookingDetails, Toast.LENGTH_LONG).show();
    }
} 