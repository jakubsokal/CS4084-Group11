package com.example.libraryapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.libraryapp.db.DatabaseHelper;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.app.AlertDialog;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.util.Log;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private static final String TAG = "BookingAdapter";
    private List<Booking> bookingList;
    private DatabaseHelper dbHelper;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking, parent, false);
        dbHelper = new DatabaseHelper(view.getContext());
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Animation animation = new Animation();
        Booking booking = bookingList.get(position);
        String floor = "Floor: " + booking.getFloorName();
        String date = "Date: " + booking.getDate();
        String seat = "Seat: " + booking.getSeatNumber();
        String room = "Room: " + booking.getRoomName();
        String time = "Time: " + booking.getStartTime() + " - " + booking.getEndTime();
        holder.floorText.setText(floor);
        holder.dateText.setText(date);
        holder.seatText.setText(seat);
        holder.roomText.setText(room);
        holder.timeText.setText(time);

        String duration = "Duration: " + calculateDuration(booking.getStartTime(), booking.getEndTime());
        holder.durationText.setText(duration);

        if (isUpcoming(booking)) {
            holder.edit_button.setVisibility(View.VISIBLE);
            holder.edit_button.setOnClickListener(v -> {
                holder.edit_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00A950")));
                animation.animateButtonTint(holder.edit_button);
                showEditDialog(v.getContext(), booking, position);
            });
        } else {
            holder.cancel_button.setVisibility(View.GONE);
            holder.edit_button.setVisibility(View.GONE);
        }

        if (isCurrentOrPast(booking)) {
            holder.cancel_button.setVisibility(View.GONE);
        } else {
            holder.cancel_button.setVisibility(View.VISIBLE);
            holder.cancel_button.setOnClickListener(v -> {
                holder.cancel_button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00A950")));
                animation.animateButtonTint(holder.cancel_button);
                if (dbHelper.cancelBooking(booking.getId())) {
                    booking.setCancelled(true);
                    notifyItemChanged(position);
                    Toast.makeText(v.getContext(), "Booking cancelled successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Failed to cancel booking", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (booking.isCancelled()) {
            holder.cancel_button.setVisibility(View.GONE);
            holder.edit_button.setVisibility(View.GONE);
        }
    }

    private void showEditDialog(Context context, Booking booking, int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_booking, null);
        TextView dateTextView = dialogView.findViewById(R.id.editDateTextView);
        TextView timeTextView = dialogView.findViewById(R.id.editTimeTextView);
        Spinner durationSpinner = dialogView.findViewById(R.id.editDurationSpinner);
        Spinner floorSpinner = dialogView.findViewById(R.id.editFloorSpinner);
        Spinner tableSpinner = dialogView.findViewById(R.id.editTableSpinner);
        Spinner seatSpinner = dialogView.findViewById(R.id.editSeatSpinner);

        dateTextView.setText(booking.getDate());
        timeTextView.setText(booking.getStartTime());


        setupSpinners(context, floorSpinner, tableSpinner, seatSpinner, durationSpinner,
                booking.getFloorName(), booking.getTableNumber(), booking.getSeatNumber(),
                calculateDuration(booking.getStartTime(), booking.getEndTime()));

        Calendar selectedDate = Calendar.getInstance();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDate.setTime(sdf.parse(booking.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateTextView.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                .format(selectedDate.getTime()));
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        Calendar selectedTime = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            selectedTime.setTime(sdf.parse(booking.getStartTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeTextView.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (view, hourOfDay, minute) -> {
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);
                        timeTextView.setText(new SimpleDateFormat("HH:mm", Locale.getDefault())
                                .format(selectedTime.getTime()));
                    },
                    selectedTime.get(Calendar.HOUR_OF_DAY),
                    selectedTime.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        });

        new AlertDialog.Builder(context)
                .setTitle("Edit Booking")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String date = dateTextView.getText().toString();
                    String time = timeTextView.getText().toString();
                    String duration = durationSpinner.getSelectedItem().toString();
                    int floor = getFloorId(floorSpinner.getSelectedItem().toString());
                    int table = getTableId(tableSpinner.getSelectedItem().toString());
                    int seat = getSeatId(seatSpinner.getSelectedItem().toString());

                    Calendar endTime = Calendar.getInstance();
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        Date startDate = sdf.parse(time);
                        endTime.setTime(startDate);
                        endTime.add(Calendar.MINUTE, getDurationInMinutes(duration));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String endTimeStr = new SimpleDateFormat("HH:mm", Locale.getDefault())
                            .format(endTime.getTime());

                    Calendar now = Calendar.getInstance();
                    Calendar bookingDateTime = Calendar.getInstance();
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        bookingDateTime.setTime(dateFormat.parse(date + " " + time));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    bookingDateTime.set(Calendar.SECOND, 0);
                    bookingDateTime.set(Calendar.MILLISECOND, 0);
                    now.set(Calendar.SECOND, 0);
                    now.set(Calendar.MILLISECOND, 0);

                    if (bookingDateTime.compareTo(now) < 0) {
                        Toast.makeText(context, "Please select a future time", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean success = dbHelper.editBooking(
                            booking.getId(), floor, table, seat, date, time, endTimeStr);
                    if (success) {
                        Toast.makeText(context, "Booking updated successfully",
                                Toast.LENGTH_SHORT).show();
                        booking.setFloorName("Floor " + floor);
                        booking.setTableNumber("Table " + table);
                        booking.setSeatNumber("Seat " + seat);
                        booking.setDate(date);
                        booking.setStartTime(time);
                        booking.setEndTime(endTimeStr);
                        notifyItemChanged(position);
                    } else {
                        Toast.makeText(context, "Failed to update booking. Time slot may be unavailable.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void setupSpinners(Context context, Spinner floorSpinner, Spinner tableSpinner,
                               Spinner seatSpinner, Spinner durationSpinner,
                               String currentFloor, String currentTable, String currentSeat,
                               String currentDuration) {
        ArrayAdapter<CharSequence> floorAdapter = ArrayAdapter.createFromResource(context,
                R.array.floors, android.R.layout.simple_spinner_item);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(floorAdapter);
        int floorPosition = getSpinnerPosition(floorSpinner, currentFloor);
        if (floorPosition >= 0) floorSpinner.setSelection(floorPosition);

        ArrayAdapter<CharSequence> tableAdapter = ArrayAdapter.createFromResource(context,
                R.array.tables, android.R.layout.simple_spinner_item);
        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableSpinner.setAdapter(tableAdapter);
        int tablePosition = getSpinnerPosition(tableSpinner, currentTable);
        if (tablePosition >= 0) tableSpinner.setSelection(tablePosition);

        ArrayAdapter<CharSequence> seatAdapter = ArrayAdapter.createFromResource(context,
                R.array.seats, android.R.layout.simple_spinner_item);
        seatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatSpinner.setAdapter(seatAdapter);
        int seatPosition = getSpinnerPosition(seatSpinner, currentSeat);
        if (seatPosition >= 0) seatSpinner.setSelection(seatPosition);

        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(context,
                R.array.durations, android.R.layout.simple_spinner_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(durationAdapter);
        int durationPosition = getSpinnerPosition(durationSpinner, currentDuration);
        if (durationPosition >= 0) durationSpinner.setSelection(durationPosition);
    }

    private int getSpinnerPosition(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                return i;
            }
        }
        return -1;
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

    private boolean isUpcoming(Booking booking) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date bookingDateTime = sdf.parse(booking.getDate() + " " + booking.getStartTime());
            Date now = new Date();

            Calendar nowPlusOneHour = Calendar.getInstance();
            nowPlusOneHour.setTime(now);
            nowPlusOneHour.add(Calendar.HOUR_OF_DAY, 1);

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
            
            return (bookingCal.getTime().equals(todayCal.getTime()) || bookingCal.getTime().after(todayCal.getTime()))
                   && bookingDateTime.after(nowPlusOneHour.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isCurrentOrPast(Booking booking) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date bookingEnd = sdf.parse(booking.getDate() + " " + booking.getEndTime());
            Date now = new Date();

            return bookingEnd.before(now);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + e.getMessage());
            return false;
        }
    }

    private String calculateDuration(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);

            if (start != null && end != null) {
                long diffInMillis = end.getTime() - start.getTime();
                long diffInHours = diffInMillis / (60 * 60 * 1000);
                long diffInMinutes = (diffInMillis / (60 * 1000)) % 60;

                if (diffInHours > 0) {
                    return diffInHours + " hour" + (diffInHours > 1 ? "s" : "") +
                            (diffInMinutes > 0 ? " " + diffInMinutes + " minute" + (diffInMinutes > 1 ? "s" : "") : "");
                } else {
                    return diffInMinutes + " minute" + (diffInMinutes > 1 ? "s" : "");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    private int getFloorId(String floorName) {
        try {
            return Integer.parseInt(floorName.substring(floorName.indexOf(" ") + 1));
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

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView floorText, dateText, seatText, roomText, timeText, durationText;
        Button edit_button, cancel_button;

        public BookingViewHolder(View itemView) {
            super(itemView);
            floorText = itemView.findViewById(R.id.floorText);
            dateText = itemView.findViewById(R.id.dateText);
            seatText = itemView.findViewById(R.id.seatText);
            roomText = itemView.findViewById(R.id.roomText);
            timeText = itemView.findViewById(R.id.timeText);
            durationText = itemView.findViewById(R.id.durationText);
            edit_button = itemView.findViewById(R.id.edit_button);
            cancel_button = itemView.findViewById(R.id.cancel_button);
        }
    }
}

