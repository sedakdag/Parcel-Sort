package com.parcel.sort.model;

import com.parcel.sort.entities.Parcel;

public class ParcelRecord {
    private String parcelID;
    private String destinationCity;
    private int priority;
    private Parcel.Size size;
    private int arrivalTime;
    private int dispatchTime;
    private int returnCount;
    private Parcel.Status status;

    public ParcelRecord(int arrivalTime) {
        this.arrivalTime = arrivalTime;
        this.returnCount = 0;
        this.status = Parcel.Status.InQueue;
    }
    public ParcelRecord(String parcelID, int arrivalTime, String destinationCity, int priority, Parcel.Size size) {
        this.parcelID = parcelID;
        this.arrivalTime = arrivalTime;
        this.destinationCity = destinationCity;
        this.priority = priority;
        this.size = size;
        this.returnCount = 0;
        this.status = Parcel.Status.InQueue;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setDispatchTime(int dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public int getDispatchTime() {
        return dispatchTime;
    }

    public int getReturnCount() {
        return returnCount;
    }

    public void incrementReturnCount() {
        returnCount++;
    }

    public Parcel.Status getStatus() {
        return status;
    }

    public void setStatus(Parcel.Status status) {
        this.status = status;
    }
    public String getParcelID() {
        return parcelID;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public int getPriority() {
        return priority;
    }

    public Parcel.Size getSize() {
        return size;
    }

    public String toString() {
        return "[" + parcelID + " | " + destinationCity + " | P" + priority + " | " + size + " | " + status + " | returns: " + returnCount + "]";
    }

}
