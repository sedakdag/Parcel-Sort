package com.parcel.sort.data_structures;

import com.parcel.sort.main.ConfigReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArrivalBuffer {
    private Node front;
    private Node rear;
    private int capacity;
    private int count;
    private int maxSize = 0;
    private ConfigReader config;

    //node class for linked list
    private class Node{
        com.parcel.sort.entities.Parcel parcel;
        com.parcel.sort.model.ParcelRecord record;
        Node next;
        Node(com.parcel.sort.entities.Parcel parcel, com.parcel.sort.model.ParcelRecord record){
            this.parcel = parcel;
            this.record = record;
            this.next = null;
        }
    }

    // constructor
    public ArrivalBuffer() {
        this.config = new ConfigReader();
        this.capacity = config.getQueueCapacity();
        this.front = null;
        this.rear = null;
        this.count = 0;
    }

    // add parcel to queue
    public void enqueue(com.parcel.sort.entities.Parcel parcel, int currentTick){
        if (isFull()) {
            System.out.println("Capacity is full! Parcel discarded: ID:" + parcel.getParcelID() + ", Destination:" + parcel.getDestinationCity());
            return;
        }
        com.parcel.sort.model.ParcelRecord record = new com.parcel.sort.model.ParcelRecord(currentTick);
        Node newNode = new Node(parcel, record);
        if(rear == null){
            front = rear = newNode;
        }
        else{
            rear.next = newNode;
            rear = newNode;
        }
        count++;
        System.out.println("Enqueued: " + parcel);
        System.out.println("Queue size: " + count);
        maxSize = Math.max(maxSize, count);
    }

    // remove parcel from queue
    public com.parcel.sort.entities.Parcel dequeue(){
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return null;
        }
        com.parcel.sort.entities.Parcel parcel = front.parcel;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        count--;
        System.out.println("Dequeued: " + parcel);
        System.out.println("Queue size: " + count);
        return parcel;
    }

    // check if queue is full
    public boolean isFull() {
        return count == capacity;
    }

    // check if queue is empty
    public boolean isEmpty() {
        return count == 0;
    }

    // peek front parcel
    public com.parcel.sort.entities.Parcel peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty!");
            return null;
        }
        return front.parcel;
    }

    // get current size
    public int size() {
        return count;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
