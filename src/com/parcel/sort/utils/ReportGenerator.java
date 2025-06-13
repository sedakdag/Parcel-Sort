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
        int totalRemaining = destinationSorter.getNodeCount() + returnStack.size();

        ParcelRecord longestDelayParcel = parcelTracker.getParcelWithLongestDelay();
        int dispatchedCount = parcelTracker.getDispatchedCount();
        int parcelsReturnedMoreThanOnce = parcelTracker.getParcelsReturnedMoreThanOnceCount();
        int numberOfEntries = parcelTracker.getSize();
        int numberOfBuckets = parcelTracker.getCapacity();
        double loadFactor = (double) numberOfEntries / numberOfBuckets;


        printReportLine("=== Final Report ===");
        printReportLine("\n\n== Simulation Overview ==");
        printReportLine("=> Total Ticks Executed:" + simulationManager.currentTick);
        printReportLine("=> Number of Parcels Generated:" + simulationManager.generatedParcels);

        printReportLine("\n== Parcel Statistics ==");
        printReportLine("=> Total Dispatched Parcels:" + dispatchedCount);
        printReportLine("=> Total Returned Parcels:" + parcelTracker.getReturnedCount());
        printReportLine("=> Number of Parcels Still in Queue/BST/Stack at End");
        printReportLine("   >In Queue (BST): " + destinationSorter.getNodeCount());
        printReportLine("   >In Return Stack: " + returnStack.size());
        printReportLine("   >Total Remaining: " + totalRemaining);

        printReportLine("\n== Destination Metrics ==");
        printReportLine("=> Number of Parcels per City");
        printReportLine("   >Parcels in Ankara: " + destinationSorter.countCityParcels("Ankara"));
        printReportLine("   >Parcels in Istanbul: " + destinationSorter.countCityParcels("Istanbul"));
        printReportLine("   >Parcels in Izmir: " + destinationSorter.countCityParcels("Izmir"));
        printReportLine("   >Parcels in Bursa: " + destinationSorter.countCityParcels("Bursa"));
        printReportLine("   >Parcels in Antalya: " + destinationSorter.countCityParcels("Antalya"));
        printReportLine("=> Most Frequently Targeted Destination:" + destinationSorter.getCityWithMostParcels());

        printReportLine("\n== Timing and Delay Metrics==");
        printReportLine("=> Average Parcel Processing Time: " + parcelTracker.getAverageProcessingTime());
        printReportLine("=> Parcel With Longest Delay: " + (longestDelayParcel != null ? longestDelayParcel.getParcelID() + " (Delay: " + (longestDelayParcel.getDispatchTime() - longestDelayParcel.getArrivalTime()) + " ticks)" : "N/A"));
        printReportLine("=> Number of Parcels Returned More Than Once: " + parcelsReturnedMoreThanOnce);

        printReportLine("\n=== Data Structure Statistics ===");
        printReportLine("=>  Maximum Queue Size Observed: " + arrivalBuffer.getMaxSize());
        printReportLine("=>  Maximum Stack Size Observed: " + returnStack.getMaxSize());
        printReportLine("=>  Final Height of BST: " + destinationSorter.getHeight());
        printReportLine("=>  Hash Table Load Factor: " + String.format("%.2f", loadFactor));
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
