package com.example.libraryapp;

public class Booking {
    private int bookingId;
    private int userId;
    private int floorId;
    private int roomId;
    private int seatId;
    private int tableId;
    private String floorName;
    private String roomName;
    private String seatNumber;
    private String tableNumber;
    private String date;
    private String startTime;
    private String endTime;
    private boolean isCancelled;
    private int duration; 

    public Booking(int bookingId, int userId, int floorId, int roomId, int seatId, int tableId,
                  String floorName, String roomName, String seatNumber, String tableNumber,
                  String date, String startTime, String endTime, boolean isCancelled, int duration) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.floorId = floorId;
        this.roomId = roomId;
        this.seatId = seatId;
        this.tableId = tableId;
        this.floorName = floorName;
        this.roomName = roomName;
        this.seatNumber = seatNumber;
        this.tableNumber = tableNumber;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isCancelled = isCancelled;
        this.duration = duration;
    }

    public int getBookingId() { return bookingId; }
    public int getId() { return bookingId; }
    public int getUserId() { return userId; }
    public int getFloorId() { return floorId; }
    public int getRoomId() { return roomId; }
    public int getSeatId() { return seatId; }
    public int getTableId() { return tableId; }
    public String getFloorName() { return floorName; }
    public String getRoomName() { return roomName; }
    public String getSeatNumber() { return seatNumber; }
    public String getTableNumber() { return tableNumber; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public boolean isCancelled() { return isCancelled; }
    public void setCancelled(boolean cancelled) { this.isCancelled = cancelled; }
    public int getDuration() { return duration; }
    public void setFloorName(String floorName) { this.floorName = floorName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public void setDate(String date) { this.date = date; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
