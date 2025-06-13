package com.parcel.sort.utils;

import com.parcel.sort.data_structures.*;
import com.parcel.sort.model.ParcelRecord;
import com.parcel.sort.entities.Parcel;
import com.parcel.sort.main.SimulationManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReportGenerator {

    private ParcelTracker parcelTracker;
    private DestinationSorter destinationSorter;
    private SimulationManager simulationManager;
    private ArrivalBuffer arrivalBuffer;
    private ReturnStack returnStack;
    private PrintWriter reportWriter;

    public ReportGenerator(ParcelTracker parcelTracker, DestinationSorter destinationSorter,
                           SimulationManager simulationManager, ArrivalBuffer arrivalBuffer, ReturnStack returnStack) {
        this.parcelTracker = parcelTracker;
        this.destinationSorter = destinationSorter;
        this.simulationManager = simulationManager;
        this.arrivalBuffer = arrivalBuffer;
        this.returnStack = returnStack;
        // initialize report writer for report.txt
        try {
            reportWriter = new PrintWriter(new FileWriter("report.txt", false)); // overwrite (false)
        } catch (IOException e) {
            System.err.println("ERROR: Could not create report file 'report.txt'. Report will only be printed to console.");
            reportWriter = null; // set to null if file creation fails
        }
    }

    // helper method to print to console and report file
    private void printReportLine(String line) {
        if (reportWriter != null) {
            reportWriter.println(line);
        }
    }

    public void generateReport() {
        int totalDispatchedParcels = parcelTracker.getDispatchedCount();
        int totalReturnedParcels = parcelTracker.getReturnedCount(); // Unique parcels returned
        int parcelsReturnedMoreThanOnce = parcelTracker.getParcelsReturnedMoreThanOnceCount();
        double averageProcessingTime = parcelTracker.getAverageProcessingTime();
        ParcelRecord longestDelayParcel = parcelTracker.getParcelWithLongestDelay();

        int parcelsInArrivalBuffer = arrivalBuffer.size();
        int parcelsInReturnStack = returnStack.size();
        int parcelsInBSTQueues = destinationSorter.getTotalParcelsInAllQueues(); // THIS SHOULD BE CORRECT
        int totalRemainingParcels = parcelsInArrivalBuffer + parcelsInReturnStack + parcelsInBSTQueues;

        int totalGenerated = simulationManager.generatedParcels;

        int numberOfEntries = parcelTracker.getSize();
        int numberOfBuckets = parcelTracker.getCapacity();
        double loadFactor = (double) numberOfEntries / numberOfBuckets;


        printReportLine("=== Final Report ===");
        printReportLine("\n\n== Simulation Overview ==");
        printReportLine("=> Total Ticks Executed:" + simulationManager.currentTick);
        printReportLine("=> Number of Parcels Generated:" + totalGenerated);

        printReportLine("\n== Parcel Statistics ==");
        printReportLine("=> Total Dispatched Parcels:" + totalDispatchedParcels);
        printReportLine("=> Total Returned Parcels:" + totalReturnedParcels);
        printReportLine("=> Number of Parcels Still in Queue/BST/Stack at End");
        printReportLine("  > In Arrival Buffer: " + parcelsInArrivalBuffer);
        printReportLine("  > In Destination Sorter (BST City Queues): " + parcelsInBSTQueues);
        printReportLine("  > In Return Stack: " + parcelsInReturnStack);
        printReportLine("  > Total Remaining in System: " + totalRemainingParcels);

        printReportLine("\n== Destination Metrics ==");
        printReportLine("=> Number of Parcels per City (currently in queues):");
        // Get city list from ConfigReader via SimulationManager for dynamic reporting
        String[] cityList = simulationManager.getConfigReader().getCityList();
        for (String city : cityList) {
            printReportLine(String.format("  > Parcels for %-10s: %d", city, destinationSorter.countCityParcels(city)));
        }
        printReportLine("=> Most Frequently Targeted Destination: " + (destinationSorter.getCityWithMostParcels() != null ? destinationSorter.getCityWithMostParcels() : "N/A"));

        printReportLine("\n== Timing and Delay Metrics==");
        printReportLine("=> Average Parcel Processing Time (for dispatched parcels): " + String.format("%.2f", averageProcessingTime) + " ticks");
        printReportLine("=> Parcel With Longest Delay: " + (longestDelayParcel != null ? longestDelayParcel.getParcelID() + " (Delay: " + (longestDelayParcel.getDispatchTime() - longestDelayParcel.getArrivalTime()) + " ticks)" : "N/A"));
        printReportLine("=> Number of Parcels Returned More Than Once: " + parcelsReturnedMoreThanOnce);

        printReportLine("\n=== Data Structure Statistics ===");
        printReportLine("=> Maximum Arrival Buffer Size Observed: " + arrivalBuffer.getMaxSize());
        printReportLine("=> Maximum Return Stack Size Observed: " + returnStack.getMaxSize());
        printReportLine("=> Final Height of BST (Destination Sorter): " + destinationSorter.getHeight());
        printReportLine("=> Hash Table Load Factor (Parcel Tracker): " + String.format("%.2f", loadFactor));
        printReportLine("==================================");

        // close the report writer to ensure all content is written to the file
        if (reportWriter != null) {
            reportWriter.flush();
            reportWriter.close();
            reportWriter = null; // Set to null after closing
            System.out.println("\nReport saved to report.txt");
        }

    }

}
