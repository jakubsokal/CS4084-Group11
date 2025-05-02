package com.example.libraryapp;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.util.Log;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.libraryapp.db.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ManageFragment extends Fragment {
    private static final String TAG = "ManageFragment";
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;
    private List<Booking> currentBookings;
    private Spinner filterSpinner;
    private DatabaseHelper dbHelper;

    public ManageFragment() {
        super(R.layout.fragment_manage_booking);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_booking, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookingList = new ArrayList<>();
        currentBookings = new ArrayList<>();

        bookingAdapter = new BookingAdapter(currentBookings);
        recyclerView.setAdapter(bookingAdapter);

        filterSpinner = view.findViewById(R.id.filterSpinner);
        String[] filterOptions = {"All", "Upcoming", "History"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, filterOptions);
        filterSpinner.setAdapter(adapter);

        loadBookings();

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = filterOptions[position];
                filterBookings(selectedFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterBookings("All");
            }
        });

        return view;
    }

    private void filterBookings(String filterType) {
        if (bookingAdapter == null) return;

        currentBookings.clear();
        if (filterType.equals("All")) {
            currentBookings.addAll(bookingList);
        } else {
            for (Booking booking : bookingList) {
                if ((filterType.equals("Upcoming") && isUpcoming(booking)) ||
                        (filterType.equals("History") && isPast(booking))) {
                    currentBookings.add(booking);
                }
            }
        }
        bookingAdapter.notifyDataSetChanged();
    }

    private boolean isUpcoming(Booking booking) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date bookingDateTime = sdf.parse(booking.getDate() + " " + booking.getStartTime());
            Date now = new Date();
            
            Calendar bookingCal = Calendar.getInstance();
            bookingCal.setTime(bookingDateTime);
            bookingCal.set(Calendar.HOUR_OF_DAY, 0);
            bookingCal.set(Calendar.MINUTE, 0);
            bookingCal.set(Calendar.SECOND, 0);
            bookingCal.set(Calendar.MILLISECOND, 0);
            
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(now);
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);
            todayCal.set(Calendar.MILLISECOND, 0);
            
            return bookingCal.getTime().after(todayCal.getTime()) || 
                   (bookingCal.getTime().equals(todayCal.getTime()) && bookingDateTime.after(now));
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + e.getMessage());
            return false;
        }
    }

    private boolean isPast(Booking booking) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date bookingDateTime = sdf.parse(booking.getDate() + " " + booking.getStartTime());
            Date now = new Date();
            
            Calendar bookingCal = Calendar.getInstance();
            bookingCal.setTime(bookingDateTime);
            bookingCal.set(Calendar.HOUR_OF_DAY, 0);
            bookingCal.set(Calendar.MINUTE, 0);
            bookingCal.set(Calendar.SECOND, 0);
            bookingCal.set(Calendar.MILLISECOND, 0);
            
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(now);
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);
            todayCal.set(Calendar.MILLISECOND, 0);
            
            return bookingCal.getTime().before(todayCal.getTime()) || 
                   (bookingCal.getTime().equals(todayCal.getTime()) && bookingDateTime.before(now));
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + e.getMessage());
            return false;
        }
    }

    private void loadBookings() {
        if (bookingList == null) {
            bookingList = new ArrayList<>();
        }
        bookingList.clear();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Log.e(TAG, "User ID not found in SharedPreferences");
            return;
        }

        List<Booking> dbBookings = dbHelper.getBookingsForUser(userId);
        if (dbBookings != null) {
            bookingList.addAll(dbBookings);
            currentBookings.clear();
            currentBookings.addAll(bookingList);
            if (bookingAdapter != null) {
                bookingAdapter.notifyDataSetChanged();
            }
        }
    }
}