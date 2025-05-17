package com.parcel.sort.model;

import com.parcel.sort.entities.Parcel;

public class ParcelRecord {
    private int arrivalTime;
    private int dispatchTime;
    private int returnCount;
    private Parcel.Status status;

    public ParcelRecord(int arrivalTime) {
        this.arrivalTime = arrivalTime;
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
}
