package com.example.libraryapp;
public class Booking {
    private String floor;
    private String date;
    private String seat;
    private String room;
    private String time;
    private String duration;
    public Booking(String floor, String date, String seat, String room, String time, String duration) {
        this.floor = floor;
        this.date = date;
        this.seat = seat;
        this.room = room;
        this.time = time;
        this.duration = duration;
    }

    public String getFloor() { return floor; }
    public String getDate() { return date; }
    public String getSeat() { return seat; }
    public String getRoom() { return room; }
    public String getTime() { return time; }
    public String getDuration() { return duration; }
}
