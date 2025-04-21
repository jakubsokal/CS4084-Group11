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
        holder.idText.setText("Booking ID: " + booking.getId());
        holder.dateText.setText("Date: " + booking.getDate());
        holder.timeText.setText("Time: " + booking.getTime());
        holder.floorText.setText("Floor: " + booking.getFloor());
        holder.tableText.setText("Table: " + booking.getTable());
        holder.seatText.setText("Seat: " + booking.getSeat());

        holder.edit_button.setOnClickListener(v -> {
            // edit logic here
        });

        holder.cancel_button.setOnClickListener(v -> {
            // delete logic here
        });
    }


    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView idText,floorText, dateText, seatText, roomText, timeText, tableText;
        Button edit_button, cancel_button;

        public BookingViewHolder(View itemView) {
            super(itemView);
            idText = itemView.findViewById(R.id.idText);
            floorText = itemView.findViewById(R.id.floorText);
            dateText = itemView.findViewById(R.id.dateText);
            seatText = itemView.findViewById(R.id.seatText);
            roomText = itemView.findViewById(R.id.roomText);
            timeText = itemView.findViewById(R.id.timeText);
            tableText = itemView.findViewById(R.id.tableText);
            edit_button = itemView.findViewById(R.id.edit_button);
            cancel_button = itemView.findViewById(R.id.cancel_button);
        }
    }
}

