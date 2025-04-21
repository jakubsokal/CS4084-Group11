package com.example.libraryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.libraryapp.Booking;
import com.example.libraryapp.R;
import com.example.libraryapp.db.DatabaseHelper;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
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
        Booking booking = bookingList.get(position);
        holder.floorText.setText("Floor: " + booking.getFloorName());
        holder.dateText.setText("Date: " + booking.getDate());
        holder.seatText.setText("Seat: " + booking.getSeatNumber());
        holder.roomText.setText("Room: " + booking.getRoomName());
        holder.timeText.setText("Time: " + booking.getStartTime() + " - " + booking.getEndTime());
        
        String duration = calculateDuration(booking.getStartTime(), booking.getEndTime());
        holder.durationText.setText("Duration: " + duration);

        if (isUpcoming(booking)) {
            holder.edit_button.setVisibility(View.VISIBLE);
            holder.edit_button.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Edit functionality coming soon", Toast.LENGTH_SHORT).show();
            });
        } else {
            holder.edit_button.setVisibility(View.GONE);
        }

        if (!booking.isCancelled()) {
            holder.cancel_button.setVisibility(View.VISIBLE);
            holder.cancel_button.setOnClickListener(v -> {
                if (dbHelper.cancelBooking(booking.getId())) {
                    booking.setCancelled(true);
                    notifyItemChanged(position);
                    Toast.makeText(v.getContext(), "Booking cancelled successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Failed to cancel booking", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.cancel_button.setVisibility(View.GONE);
        }
    }

    private boolean isUpcoming(Booking booking) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date bookingDate = sdf.parse(booking.getDate());
            return bookingDate != null && bookingDate.after(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
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

