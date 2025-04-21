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


import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ManageFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList;

    private List<Booking> currentBookings;
    private Spinner filterSpinner;
    public ManageFragment() {
        super(R.layout.fragment_manage_booking);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_booking, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //Booked booked= new Booked(getContext());
        //bookingList = booked.getAllBookings() ;
        //

        // Set up filter spinner
        filterSpinner = view.findViewById(R.id.filterSpinner);
        String[] filterOptions = {"All", "Upcoming", "History"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, filterOptions);
        filterSpinner.setAdapter(spinnerAdapter);

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

    //id fate comes after the current date its added to upcoming
    private boolean isUpcoming(Booking booking) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date bookingDate = sdf.parse(booking.getDate());
            return bookingDate != null && bookingDate.after(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    //if date is before current date it's added to history
    private boolean isPast(Booking booking) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date bookingDate = sdf.parse(booking.getDate());
            return bookingDate != null && bookingDate.before(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}