package com.parcel.sort.data_structures;
import com.parcel.sort.entities.Parcel;

public class ReturnStack {

     //each node holds a parcel and points to the next one
    private static class Node{
        Parcel parcel;
        Node next;
        Node(Parcel parcel){
            this.parcel = parcel;
        }

    }
    private Node top; //top of the stack
    private int size; //number of parcels
    private int maxSize = 0;

    // constructor
    public ReturnStack() {
        this.top = null;
        this.size = 0;
    }

    //adds parcel to the top
    public void push(Parcel parcel){
        Node newNode = new Node(parcel);
        newNode.next = top;
        top = newNode;
        size++;

        if (size > maxSize) {
            maxSize = size;
        }
    }
    //removes and returns the top parcel, null if empty
    public Parcel pop(){
        if(top == null){
            return null;
        }
        Parcel result = top.parcel;
        top = top.next;
        size--;
        return result;
    }
    //top parcel return
    public Parcel peek() {
        if (top == null) {
            return null;
        }
        return top.parcel;
    }
    // checks if empty
    public boolean isEmpty(){
        return top == null;
    }
    //returns how many parcels there are
    public int size(){
        return size;
    }

    public int getMaxSize() {
        return maxSize;
    }
}


