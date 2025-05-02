package com.example.libraryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {
    private List<AlertItem> alerts;
    private final OnAlertClickListener listener;

    public interface OnAlertClickListener {
        void onAlertClick(AlertItem alert);
    }

    public AlertAdapter(List<AlertItem> alerts, OnAlertClickListener listener) {
        this.alerts = alerts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alert, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        AlertItem alert = alerts.get(position);
        holder.bind(alert);
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public void updateAlerts(List<AlertItem> newAlerts) {
        this.alerts = newAlerts;
        notifyDataSetChanged();
    }

    public class AlertViewHolder extends RecyclerView.ViewHolder {
        private TextView alertTypeText;
        private TextView messageText;
        private TextView createdAtText;
        private View unreadIndicator;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            alertTypeText = itemView.findViewById(R.id.alertTypeText);
            messageText = itemView.findViewById(R.id.messageText);
            createdAtText = itemView.findViewById(R.id.createdAtText);
            unreadIndicator = itemView.findViewById(R.id.unreadIndicator);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAlertClick(alerts.get(position));
                }
            });
        }

        public void bind(AlertItem alert) {
            alertTypeText.setText(alert.getAlertType());
            messageText.setText(alert.getMessage());
            createdAtText.setText(alert.getCreatedAt());
            unreadIndicator.setVisibility(alert.isRead() ? View.GONE : View.VISIBLE);
        }
    }
} 