package com.example.libraryapp;

//changing fields to match whats in db
public class Booking {
    private int id;
    private String date;
    private String time;
    private String floor;
    private String table;
    private String seat;

    public Booking(int id, String date, String time, String floor, String table, String seat){
        this.id = id;
        this.date = date;
        this.time = time;
        this.floor = floor;
        this.table = table;
        this.seat = seat;
    }

    public int getId() { return id; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getFloor() { return floor; }
    public String getTable() { return table; }
    public String getSeat() { return seat; }
}
