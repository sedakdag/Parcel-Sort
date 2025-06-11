package com.parcel.sort.data_structures;
import com.parcel.sort.entities.Parcel;
import com.parcel.sort.model.ParcelRecord;
//tek tek anlayıp açıklayacağım relax takıl babaaaa
public class ParcelTracker {
    private static class Node{
        String key;
        ParcelRecord value;
        Node next;
        Node(String key, ParcelRecord value) {
            this.key = key;
            this.value = value;
        }
    }
    private int dispatchedCount = 0;
    private int returnedCount= 0;
    private Node[] table;
    private  int capacity;
    private  int size;
    public ParcelTracker(int capacity) {
        this.capacity = capacity;
        this.table = new Node[capacity];
        this.size = 0;
    }
    private int hash(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash += key.charAt(i) * (i + 1);
        }
        return Math.abs(hash) % capacity;
    }
    public boolean exists(String parcelID) {
        return get(parcelID) != null;
    }


    public void insert(String parcelID, ParcelRecord record) {
        int index = hash(parcelID);
        Node current = table[index];


        while (current != null) {
            if (current.key.equals(parcelID)) {
                current.value = record;
                return;
            }

            current = current.next;
        }


        Node newNode = new Node(parcelID, record);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }


    public ParcelRecord get(String parcelID) {
        int index = hash(parcelID);
        Node current = table[index];

        while (current != null) {
            if (current.key.equals(parcelID)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    public void updateStatus(String parcelID, Parcel.Status newStatus, Integer dispatchTime) {
        ParcelRecord record = get(parcelID);
        if (record != null) {
            if (record.getStatus() != newStatus) {
                if (newStatus == Parcel.Status.Dispatched) {
                    dispatchedCount++;

                }
                else if (newStatus == Parcel.Status.Returned) {
                    returnedCount++;
                }
            }
            record.setStatus(newStatus);
            if (newStatus == Parcel.Status.Dispatched && dispatchTime != null) {
                record.setDispatchTime(dispatchTime);
            }
        }
    }


    public void incrementReturnCount(String parcelID) {
        ParcelRecord record = get(parcelID);
        if (record != null) {
            record.incrementReturnCount();
        }
    }


    public void printAll() {
        for (int i = 0; i < capacity; i++) {
            Node current = table[i];
            while (current != null) {
                System.out.println(current.value);  // uses toString() from ParcelRecord
                current = current.next;
            }
        }
    }

    public double getAverageProcessingTime() {
        int totalTime = 0;
        int count = 0;

        for (int i = 0; i < capacity; i++) {
            Node current = table[i];
            while (current != null) {
                ParcelRecord record = current.value;
                if (record.getStatus() == Parcel.Status.Dispatched &&
                        record.getDispatchTime() > 0) {
                    totalTime += (record.getDispatchTime() - record.getArrivalTime());
                    count++;
                }
                current = current.next;
            }
        }

        if (count == 0){
            return 0.0;
        }
        else {
            return (double) totalTime / count;
        }
    }

    // finds the parcel record with the longest processing delay
    public ParcelRecord getParcelWithLongestDelay() {
        ParcelRecord longestDelayParcel = null;
        int maxDelay = -1; // initialize with a value that any valid delay will exceed

        for (int i = 0; i < capacity; i++) {
            Node current = table[i];
            while (current != null) {
                ParcelRecord record = current.value;
                // only consider dispatched parcels with valid dispatch times
                if (record.getStatus() == Parcel.Status.Dispatched &&
                        record.getDispatchTime() > 0) {
                    int currentDelay = record.getDispatchTime() - record.getArrivalTime();
                    if (currentDelay > maxDelay) {
                        maxDelay = currentDelay;
                        longestDelayParcel = record;
                    }
                }
                current = current.next;
            }
        }
        return longestDelayParcel;
    }

    // counts parcels that have been returned more than once
    public int getParcelsReturnedMoreThanOnceCount() {
        int count = 0;
        for (int i = 0; i < capacity; i++) {
            Node current = table[i];
            while (current != null) {
                if (current.value.getReturnCount() > 1) { // check if return count is greater than 1
                    count++;
                }
                current = current.next;
            }
        }
        return count;
    }

    public int getDispatchedCount() {
        return dispatchedCount;
    }
    public int getReturnedCount() {
        return returnedCount;
    }
    public int getSize() {
        return size;
    }
    public int getCapacity() {
        return capacity;
    }

    // returns a collection of all parcel records for report generation
    public java.util.Collection<ParcelRecord> getAllRecords() {
        java.util.List<ParcelRecord> allRecords = new java.util.ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            Node current = table[i];
            while (current != null) {
                allRecords.add(current.value);
                current = current.next;
            }
        }
        return allRecords;
    }
}
