package com.parcel.sort.data_structures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TerminalRotator {
    private Node current;

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
    public TerminalRotator(String[] cityList) {
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
    }

    // advance to the next terminal
    public void advanceTerminal() {
        if (current != null) {
            current = current.next;
            System.out.println("Rotated to: " + current.cityName);
        }
    }

    // get the current active terminal
    public String getActiveTerminal() {
        if (current != null) {
            System.out.println("Current city: ");
            return current.cityName;
        } else {
            System.out.println("There is no current city.");
            return null;
        }
    }

    // print all terminals in rotation order
    public void printTerminalOrder() {
        if (current == null) return;
        Node temp = current;
        do {
            System.out.print(temp.cityName + " -> ");
            temp = temp.next;
        } while (temp != current);
        System.out.println("(back to start)");
    }

    // read city list from config.txt
    public static String[] readCityList() {
        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("CITY LIST=")) {
                    return line.split("=")[1].trim().split(", ");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading city list from config file.");
        }
        return new String[] {"Istanbul", "Ankara", "Izmir", "Bursa", "Antalya"}; // default list
    }
}
