package com.parcel.sort.data_structures;

import com.parcel.sort.main.Logger;

public class TerminalRotator {
    private Node current;
    private Logger logger;

    // node class for circular linked list
    private class Node {
        String cityName;
        Node next;
        Node(String cityName) {
            this.cityName = cityName;
            this.next = this; // circular reference
        }
    }

    // constructor
    public TerminalRotator(String[] cityList, Logger logger) {
        this.logger = logger;
        if (cityList.length == 0) {
            throw new IllegalArgumentException("City list cannot be empty.");
        }
        initializeFromCityList(cityList);
    }

    // initialize the circular linked list
    private void initializeFromCityList(String[] cityList) {
        Node first = new Node(cityList[0]);
        Node prev = first;
        for (int i = 1; i < cityList.length; i++) {
            Node newNode = new Node(cityList[i]);
            prev.next = newNode;
            prev = newNode;
        }
        prev.next = first; // complete the circle
        current = first;

        logger.logTerminalChange("Initial Active Terminal: " + current.cityName);
    }

    // advance to the next terminal
    public void advanceTerminal() {
        if (current != null) {
            current = current.next;
        }
    }

    // get the current active terminal
    public String getActiveTerminal() {
        if (current != null) {
            return current.cityName;
        } else {
            return null;
        }
    }

    // print all terminals in rotation order
    public void printTerminalOrder() {
        if (current == null) return;
        Node temp = current;
        StringBuilder sb = new StringBuilder("Terminal Order: ");
        do {
            sb.append(temp.cityName);
            if (temp.next != current) {
                sb.append(" -> ");
            }
            temp = temp.next;
        } while (temp != current);
        sb.append(" (back to start)");
    }
}
