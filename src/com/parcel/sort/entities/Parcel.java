package com.parcel.sort.entities;

import java.util.Random;

public class Parcel {
    private static int idCounter = 100; // to generate unique id

    private static String parcelID; // unique id
    private static String destinationCity;
    private static int priority;
    private int arrivalTick;


    // enum for parcel status
    public enum Status {
        InQueue, Sorted, Dispatched, Returned
    }

    private Status status;

    public enum Size {
        Small, Medium, Large
    }

    private static Size size;

    // constructors
    public Parcel(String parcelID, String destinationCity, int priority, Size size, int arrivalTick, Status status) {
        this.parcelID = parcelID;
        this.destinationCity = destinationCity;
        this.priority = priority;
        this.arrivalTick = arrivalTick;
        this.status = status;
    }

    public static Parcel generateRandomParcel(int currentTick, String[] cityList) {
        Random random = new Random();

        parcelID = "P" + idCounter++;
        destinationCity = cityList[random.nextInt(cityList.length)];
        priority = random.nextInt(3) + 1; // 1, 2, 3
        size = Size.values()[random.nextInt(Size.values().length)];

        return new Parcel(parcelID ,destinationCity, priority, size, currentTick, Status.InQueue);
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
}
