package com.parcel.sort.data_structures;

public class ReturnStack<T> {

    private Node<T> top;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) {
            this.data = data;
        }
    }

    public void push(T data) {
        Node<T> node = new Node<>(data);
        node.next = top;
        top = node;
        size++;
    }

    public T pop() {
        if (top==null) {
            return null;
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        if (top == null) return null;
        return top.data;
    }

    //Check if the stack is empty
    public boolean isEmpty() {
        return top == null;
    }

    //Return the number of elements
    public int size() {
        return size;
    }
}