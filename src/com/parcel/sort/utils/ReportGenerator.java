package com.parcel.sort.utils;

import com.parcel.sort.data_structures.*;
import com.parcel.sort.model.ParcelRecord;
import com.parcel.sort.entities.Parcel;
import com.parcel.sort.main.SimulationManager;

public class ReportGenerator {

    private ParcelTracker parcelTracker;
    private DestinationSorter destinationSorter;
    private SimulationManager simulationManager;
    private ArrivalBuffer arrivalBuffer;
    private ReturnStack returnStack;
    private ParcelRecord parcelRecord;

    public ReportGenerator(ParcelTracker parcelTracker, DestinationSorter destinationSorter) {
        this.parcelTracker = parcelTracker;
        this.destinationSorter = destinationSorter;
        this.simulationManager = simulationManager;
        this.arrivalBuffer = arrivalBuffer;
        this.returnStack = returnStack;
        this.parcelRecord = parcelRecord;
    }

    public void generateReport() {
        int totalParcels = 0;
        int dispatchedCount = 0;
        int totalWaitTime = 0;
        int maxWaitTime = -1;
        String maxWaitParcelID = null;

        int totalRemaining = destinationSorter.getNodeCount() + returnStack.size();


        int numberOfEntries = parcelTracker.getSize();
        int numberOfBuckets = parcelTracker.getCapacity();
        double loadFactor = (double) numberOfEntries / numberOfBuckets;


        System.out.println("=== Final Report ===");
        System.out.println("== Simulation Overview ==");
        System.out.println("=> Total Ticks Executed:" + simulationManager.currentTick);
        System.out.println("=> Number of Parcels Generated:" + simulationManager.generatedParcels);
        System.out.println("== Parcel Statistics ==");
        System.out.println("=> Total Dispatched Parcels:" + dispatchedCount);
        System.out.println("=> Total Returned Parcels:" + parcelTracker.getReturnedCount());
        System.out.println("=> Number of Parcels Still in Queue/BST/Stack at End:");
        System.out.println(">In Queue (BST): " + destinationSorter.getNodeCount());
        System.out.println(">In Return Stack: " + returnStack.size());
        System.out.println(">Total Remaining: " + totalRemaining);
        System.out.println("== Destination Metrics ==");
        System.out.println("=> Number of Parcels per City:");
        System.out.println(">Parcels in Ankara: " + destinationSorter.countCityParcels("Ankara"));
        System.out.println(">Parcels in Istanbul: " + destinationSorter.countCityParcels("Istanbul"));
        System.out.println(">Parcels in Izmir: " + destinationSorter.countCityParcels("Izmir"));
        System.out.println(">Parcels in Bursa: " + destinationSorter.countCityParcels("Bursa"));
        System.out.println(">Parcels in Antalya: " + destinationSorter.countCityParcels("Antalya"));
        System.out.println("=> Most Frequently Targeted Destination:" + destinationSorter.getCityWithMostParcels());
        System.out.println("== Timing and Delay Metrics==");
        System.out.println("=> Average Parcel Processing Time:" + parcelTracker.getAverageProcessingTime());
        System.out.println("=> Parcel With Longest Delay:");
        System.out.println("=> Number of Parcels Returned More Than Once:");
        System.out.println("=== Data Structure Statistics ===");
        System.out.println("=>  Maximum Queue Size Observed" + arrivalBuffer.getMaxSize());
        System.out.println("=>  Maximum Stack Size Observed" + returnStack.getMaxSize());
        System.out.println("=>  Final Height of BST" + destinationSorter.getHeight());
        System.out.println("=>  Hash Table Load Factor" + loadFactor);

    }

}
