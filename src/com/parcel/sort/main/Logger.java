package com.parcel.sort.main;

import com.parcel.sort.entities.Parcel;
import java.io.FileWriter;   // Used to write to a text file
import java.io.IOException; // Handles file writing errors
import java.io.PrintWriter; // Allows writing lines to the file

public class Logger {
    private PrintWriter writer;


    public Logger(String fileName){
        try {
            writer = new PrintWriter(new FileWriter(fileName, false)); // true = append mode
        } catch (IOException e) {
            System.out.println("Could not create log file.");
            e.printStackTrace(); // Print error details
        }
    }

    // Logs the start of a simulation tick
    public void logTickStart(int tick){
        String msg ="\n[Tick #" + tick + "]: ";
        println(msg); // Write to console and file
    }

    // Logs a new parcel arrival with ID, city, and priority
    public void logParcelArrival(Parcel parcel){
        String msg = "New Parcel: " + parcel.getParcelID() + " to " + parcel.getDestinationCity() + " (Priority: " + parcel.getPriority() + ")";
        println(msg);
    }

    // Logs the current size of the arrival queue
    public void logQueueSize(int size){
        String msg = "Queue Size: " + size;
        println(msg);
    }

    // Logs a parcel being sorted into the BST
    public void logBSTInsertion(Parcel parcel) {
        String msg = "Sorted to BST: " + parcel.getParcelID();
        println(msg);
    }

    // Logs status changes like SORTED, RETURNED, DISPATCHED
    public void logStatusChange(String parcelID, Parcel.Status newStatus) {
        String msg = "Status Update: Parcel " + parcelID + " -> " + newStatus;
        println(msg);
    }


    // Logs a successful dispatch from BST to a terminal
    public void logDispatchSuccess(Parcel parcel, String terminal) {
        String msg = "Dispatched: " + parcel.getParcelID() + " from BST to " + terminal + " -> SUCCESS";
        println(msg);
    }

    // Logs a failed dispatch attempt from BST to a terminal
    public void logDispatchFailure(Parcel parcel, String terminal) {
        String msg = "Dispatched: " + parcel.getParcelID() + " from BST to " + terminal + " -> FAILED";
        println(msg);
    }

    // Logs when a parcel is returned to the return stack due to failure
    public void logReturn(Parcel parcel, int returnCount) {
        String msg = "Returned: " + parcel.getParcelID() + " misrouted -> RETURNED to Stack (ReturnCount: " + returnCount + ")";
        println(msg);
    }

    // Logs the current number of items in the return stack
    public void logStackSize(int size) {
        String msg = "ReturnStack Size: " + size;
        println(msg);
    }

    // Logs when the active terminal changes
    public void logTerminalChange(String terminal) {
        String msg = "Rotated to: " + terminal;
        println(msg);
    }

    // Helper function: prints to both console and file
    private void println(String msg){
        System.out.println(msg); // Console
        if(writer != null){
            writer.println(msg); // File
            writer.flush(); // Makes sure it is written immediately
        }
    }

    // Closes the writer to finish writing safely
    public void close() {
        if (writer != null) {
            writer.close(); // Close file when done
            writer = null;
        }
    }
}


