package com.example.libraryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.libraryapp.Booking;
import com.example.libraryapp.R;
import java.util.List;
//Just making this class so that the booking component will be able to get data from db,whne its implemented.
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookingList;
    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.floorText.setText("Floor: " + booking.getFloor());
        holder.dateText.setText("Date: " + booking.getDate());
        holder.seatText.setText("Seat: " + booking.getSeat());
        holder.roomText.setText("Room: " + booking.getRoom());
        holder.timeText.setText("Time: " + booking.getTime());
        holder.durationText.setText("Duration: " + booking.getDuration());


        holder.edit_button.setOnClickListener(v -> {

        });

        holder.cancel_button.setOnClickListener(v -> {
            // will remove the booking from db when implemeted
        });
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

