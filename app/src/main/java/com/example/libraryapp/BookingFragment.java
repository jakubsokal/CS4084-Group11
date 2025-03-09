package com.example.libraryapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import com.example.libraryapp.db.booked.Bookings;

public class BookingFragment extends Fragment implements SeatMapView.OnSeatSelectedListener {
    private Button btnDatePicker;
    private Button btnTimePicker;
    private Spinner spinnerFloor;
    private SeatMapView seatMapView;
    private TextView tvSelectedSeat;
    private Button btnBook;
    private Bookings bookingsDb;

    private Calendar selectedDateTime;
    private String selectedSeatId;

    public BookingFragment() {
        // Required empty public constructor
    }

    public static BookingFragment newInstance() {
        return new BookingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedDateTime = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        try {
            initializeViews(view);
            setupFloorSpinner();
            setupSeatMap(view);
            setupListeners();
            updateDateButtonText();
            updateTimeButtonText();
            updateAvailableSeats();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error initializing booking: " + e.getMessage(), 
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return view;
    }

    private void initializeViews(View view) {
        bookingsDb = new Bookings(requireContext());
        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        btnTimePicker = view.findViewById(R.id.btnTimePicker);
        spinnerFloor = view.findViewById(R.id.spinnerFloor);
        tvSelectedSeat = view.findViewById(R.id.tvSelectedSeat);
        btnBook = view.findViewById(R.id.btnBook);
    }

    private void setupFloorSpinner() {
        String[] floors = new String[]{"Floor 1", "Floor 2", "Floor 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, floors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFloor.setAdapter(adapter);
    }

    private void setupSeatMap(View view) {
        ViewGroup seatMapContainer = view.findViewById(R.id.seatMapContainer);
        seatMapView = new SeatMapView(requireContext());
        seatMapContainer.addView(seatMapView);
        seatMapView.setOnSeatSelectedListener(this);
    }

    private void setupListeners() {
        setupDatePicker();
        setupTimePicker();
        setupFloorListener();
        setupBookButton();
    }

    private void setupDatePicker() {
        btnDatePicker.setOnClickListener(v -> {
            try {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, month);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateButtonText();
                        updateAvailableSeats();
                    },
                    selectedDateTime.get(Calendar.YEAR),
                    selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Error showing date picker", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupTimePicker() {
        btnTimePicker.setOnClickListener(v -> {
            try {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view, hourOfDay, minute) -> {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);
                        updateTimeButtonText();
                        updateAvailableSeats();
                    },
                    selectedDateTime.get(Calendar.HOUR_OF_DAY),
                    selectedDateTime.get(Calendar.MINUTE),
                    true
                );
                timePickerDialog.show();
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Error showing time picker", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFloorListener() {
        spinnerFloor.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateAvailableSeats();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void setupBookButton() {
        btnBook.setOnClickListener(v -> attemptBooking());
    }

    private void updateAvailableSeats() {
        if (seatMapView != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String date = dateFormat.format(selectedDateTime.getTime());
                String time = timeFormat.format(selectedDateTime.getTime());

                Calendar endTime = (Calendar) selectedDateTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY, 2);
                String endTimeStr = timeFormat.format(endTime.getTime());

                int floorNumber = spinnerFloor.getSelectedItemPosition() + 1;

                for (SeatMapView.Seat seat : seatMapView.getSeats()) {
                    boolean isAvailable = bookingsDb.isSeatAvailable(
                        floorNumber, 
                        seat.id, 
                        date, 
                        time, 
                        endTimeStr
                    );
                    seat.isAvailable = isAvailable;
                }
                seatMapView.invalidate();
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Error updating seats", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void attemptBooking() {
        try {
            if (selectedSeatId == null) {
                Toast.makeText(requireContext(), "Please select a seat", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            
            String date = dateFormat.format(selectedDateTime.getTime());
            String startTime = timeFormat.format(selectedDateTime.getTime());
            
            Calendar endTime = (Calendar) selectedDateTime.clone();
            endTime.add(Calendar.HOUR_OF_DAY, 2);
            String endTimeStr = timeFormat.format(endTime.getTime());

            int floorNumber = spinnerFloor.getSelectedItemPosition() + 1;

            // Check if seat is still available
            if (!bookingsDb.isSeatAvailable(floorNumber, selectedSeatId, date, startTime, endTimeStr)) {
                Toast.makeText(requireContext(), "Sorry, this seat was just booked. Please select another.", Toast.LENGTH_LONG).show();
                updateAvailableSeats();
                return;
            }

            // Get the current user's ID from the session or preferences
            // TODO: Replace with actual user ID from session management
            int currentUserId = 1;

            long bookingId = bookingsDb.createBooking(
                currentUserId,
                floorNumber,
                selectedSeatId,
                date,
                startTime,
                endTimeStr
            );

            if (bookingId != -1) {
                String confirmMessage = String.format(Locale.getDefault(),
                    "Booking confirmed!\nDate: %s\nTime: %s - %s\nFloor: %d\nSeat: %s",
                    date, startTime, endTimeStr, floorNumber, selectedSeatId);
                Toast.makeText(requireContext(), confirmMessage, Toast.LENGTH_LONG).show();
                
                updateAvailableSeats();
            } else {
                Toast.makeText(requireContext(), "Failed to create booking. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error creating booking: " + e.getMessage(), 
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSeatSelected(SeatMapView.Seat seat) {
        selectedSeatId = seat.id;
        tvSelectedSeat.setText(String.format("Selected Seat: %s", selectedSeatId));
    }

    private void updateDateButtonText() {
        btnDatePicker.setText(String.format(Locale.getDefault(), "%tF", selectedDateTime));
    }

    private void updateTimeButtonText() {
        btnTimePicker.setText(String.format(Locale.getDefault(), "%tR", selectedDateTime));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bookingsDb != null) {
            bookingsDb.close();
        }
    }
}
