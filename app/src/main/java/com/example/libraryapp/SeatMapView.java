package com.example.libraryapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class SeatMapView extends View {
    private Paint paint;
    private List<Seat> seats;
    private Seat selectedSeat;
    private OnSeatSelectedListener listener;

    public interface OnSeatSelectedListener {
        void onSeatSelected(Seat seat);
    }

    public static class Seat {
        public final int row;
        public final int column;
        public final String id;
        public boolean isAvailable;
        RectF rect;

        public Seat(int row, int column, boolean isAvailable) {
            this.row = row;
            this.column = column;
            this.id = "S" + row + "-" + column;
            this.isAvailable = isAvailable;
            this.rect = new RectF();
        }
    }

    public SeatMapView(Context context) {
        super(context);
        init();
    }

    public SeatMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        seats = new ArrayList<>();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 6; col++) {
                seats.add(new Seat(row, col, true));
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float seatWidth = w / 7f;
        float seatHeight = h / 5f;
        
        for (Seat seat : seats) {
            float left = (seat.column + 0.5f) * seatWidth;
            float top = (seat.row + 0.5f) * seatHeight;
            seat.rect = new RectF(left, top, left + seatWidth * 0.8f, top + seatHeight * 0.8f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        
        float cellWidth = getWidth() / 7f;
        float cellHeight = getHeight() / 5f;

        for (int i = 1; i < 7; i++) {
            float x = i * cellWidth;
            canvas.drawLine(x, 0, x, getHeight(), paint);
        }

        for (int i = 1; i < 5; i++) {
            float y = i * cellHeight;
            canvas.drawLine(0, y, getWidth(), y, paint);
        }

        paint.setStyle(Paint.Style.FILL);
        for (Seat seat : seats) {
            if (seat == selectedSeat) {
                paint.setColor(Color.GREEN);
            } else if (seat.isAvailable) {
                paint.setColor(Color.BLUE);
            } else {
                paint.setColor(Color.RED);
            }
            canvas.drawRoundRect(seat.rect, 8, 8, paint);
            
            paint.setColor(Color.WHITE);
            paint.setTextSize(24);
            paint.setTextAlign(Paint.Align.CENTER);
            float textX = seat.rect.centerX();
            float textY = seat.rect.centerY() + paint.getTextSize() / 3;
            canvas.drawText(seat.id, textX, textY, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            
            for (Seat seat : seats) {
                if (seat.rect.contains(x, y) && seat.isAvailable) {
                    selectedSeat = seat;
                    if (listener != null) {
                        listener.onSeatSelected(seat);
                    }
                    invalidate();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnSeatSelectedListener(OnSeatSelectedListener listener) {
        this.listener = listener;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeatAvailability(int row, int column, boolean isAvailable) {
        for (Seat seat : seats) {
            if (seat.row == row && seat.column == column) {
                seat.isAvailable = isAvailable;
                invalidate();
                break;
            }
        }
    }
} 