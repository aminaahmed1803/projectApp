package com.example.projectapp;

public class RoomObject {
    protected String roomName, dorm, avail;
    protected int capacity, floor, timeSlots, numReserve;

    public RoomObject(String rn, int c, String d, int f, int ts, String a, int nr){
        this.roomName = rn;
        this.capacity = c;
        this.dorm = d;
        this.floor = f;
        this.timeSlots = ts;
        this.avail = a;
        this.numReserve = nr;
    }
}