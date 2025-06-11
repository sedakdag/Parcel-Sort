package com.parcel.sort.data_structures;

import com.parcel.sort.entities.Parcel;
import com.parcel.sort.main.ConfigReader;

public class ArrivalBuffer {
    private Node front;
    private Node rear;
    private int capacity;
    private int count;
    private int maxSize = 0;
    private ConfigReader config;

    //node class for linked list
    private class Node{
        Parcel parcel;
        Node next;
        Node(Parcel parcel) {
            this.parcel = parcel;
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
        Node newNode = new Node(parcel);
        if(rear == null){
            front = rear = newNode;
        }
        else{
            rear.next = newNode;
            rear = newNode;
        }
        count++;
        maxSize = Math.max(maxSize, count);
    }

    // remove parcel from queue
    public com.parcel.sort.entities.Parcel dequeue(){
        if (isEmpty()) {
            return null;
        }
        com.parcel.sort.entities.Parcel parcel = front.parcel;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        count--;
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
    public Parcel peek() {
        if (isEmpty()) {
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
