package com.parcel.sort.entities;

public class Parcel {
    private int parcelID; // unique id
    private String destinationCity;
    private int priority;
    private String size;
    private int arrivalTick;

    // enum for parcel status
    public enum Status {
        InQueue, Sorted, Dispatched, Returned
    }

    private Status status;

    // constructors
    public Parcel(int parcelID, String destinationCity, int priority, String size, int arrivalTick, Status status) {
        this.parcelID = parcelID;
        this.destinationCity = destinationCity;
        this.priority = priority;
        this.size = size;
        this.arrivalTick = arrivalTick;
        this.status = status;
    }

    public int getParcelID() {
        return parcelID;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

}
