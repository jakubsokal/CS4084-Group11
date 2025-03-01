package com.example.libraryapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashSet;
import java.util.Set;

public class BookingFragment extends Fragment {

    private Spinner floorSpinner, deskSpinner, seatSpinner;
    private TextView selectedSeatText;
    private Button bookSeatButton;
    private Set<String> bookedSeats;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        // Initialize UI elements
        floorSpinner = view.findViewById(R.id.floorSpinner);
        deskSpinner = view.findViewById(R.id.deskSpinner);
        seatSpinner = view.findViewById(R.id.seatSpinner);
        selectedSeatText = view.findViewById(R.id.selectedSeatText);
        bookSeatButton = view.findViewById(R.id.bookSeatButton);

        // Load booked seats from SharedPreferences
        loadBookedSeats();

        setupFloorSpinner();

        // Handle seat booking
        bookSeatButton.setOnClickListener(v -> {
            String selectedSeat = selectedSeatText.getText().toString().replace("Selected Seat: ", "");
            if (!selectedSeat.equals("None") && !bookedSeats.contains(selectedSeat)) {
                saveBookedSeat(selectedSeat);
                selectedSeatText.setText("Seat Booked: " + selectedSeat);
                bookSeatButton.setEnabled(false);
                Toast.makeText(requireContext(), "Seat booked successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Seat already booked!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void setupFloorSpinner() {
        String[] floors = {"1st Floor", "2nd Floor", "3rd Floor"};
        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, floors);
        floorSpinner.setAdapter(floorAdapter);

        floorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupDeskSpinner(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupDeskSpinner(int floor) {
        String[] desks = {"Desk 1", "Desk 2", "Desk 3", "Desk 4"};
        ArrayAdapter<String> deskAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, desks);
        deskSpinner.setAdapter(deskAdapter);

        deskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupSeatSpinner(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSeatSpinner(int desk) {
        String[] seats = {desk + "A", desk + "B", desk + "C", desk + "D", desk + "E"};
        ArrayAdapter<String> seatAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, seats);
        seatSpinner.setAdapter(seatAdapter);

        seatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seat = (String) parent.getItemAtPosition(position);
                selectedSeatText.setText("Selected Seat: " + seat);
                bookSeatButton.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadBookedSeats() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("seat_prefs", Context.MODE_PRIVATE);
        bookedSeats = new HashSet<>(prefs.getStringSet("booked_seats", new HashSet<>()));
    }

    private void saveBookedSeat(String seat) {
        bookedSeats.add(seat);
        SharedPreferences prefs = requireActivity().getSharedPreferences("seat_prefs", Context.MODE_PRIVATE);
        prefs.edit().putStringSet("booked_seats", bookedSeats).apply();
    }
}
