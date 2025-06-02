package com.parcel.sort.entities;

import java.util.Random;

public class Parcel {
    private static int idCounter = 100; // unique ID counter for all parcels

    private String parcelID;
    private String destinationCity;
    private int priority;
    private int arrivalTick;
    private Status status;
    private Size size;

    public enum Status {
        InQueue, Sorted, Dispatched, Returned
    }

    public enum Size {
        Small, Medium, Large
    }

    public Parcel(String parcelID, String destinationCity, int priority, Size size, int arrivalTick, Status status) {
        this.parcelID = parcelID;
        this.destinationCity = destinationCity;
        this.priority = priority;
        this.arrivalTick = arrivalTick;
        this.status = status;
        this.size = size;
    }

    public static Parcel generateRandomParcel(int currentTick, String[] cityList) {
        Random random = new Random();
        String id = "P" + idCounter++;
        String city = cityList[random.nextInt(cityList.length)];
        int pr = random.nextInt(3) + 1;
        Size sz = Size.values()[random.nextInt(Size.values().length)];

        return new Parcel(id, city, pr, sz, currentTick, Status.InQueue);
    }

    // Getter methods
    public String getParcelID() {
        return parcelID;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public int getPriority() {
        return priority;
    }

    public int getArrivalTick() {
        return arrivalTick;
    }

    public Status getStatus() {
        return status;
    }

    public Size getSize() {
        return size;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
