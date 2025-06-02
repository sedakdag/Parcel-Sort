package com.parcel.sort.utils;

import com.parcel.sort.data_structures.ParcelTracker;
import com.parcel.sort.data_structures.DestinationSorter;
import com.parcel.sort.model.ParcelRecord;
import com.parcel.sort.entities.Parcel;

public class ReportGenerator {

    private ParcelTracker parcelTracker;
    private DestinationSorter destinationSorter;

    public ReportGenerator(ParcelTracker parcelTracker, DestinationSorter destinationSorter) {
        this.parcelTracker = parcelTracker;
        this.destinationSorter = destinationSorter;
    }

    public void generateReport() {
        int totalParcels = 0;
        int dispatchedCount = 0;
        int totalWaitTime = 0;
        int maxWaitTime = -1;
        String maxWaitParcelID = null;


        for (int i = 0; i < parcelTracker.getCapacity(); i++) {
            var node = parcelTracker.getTable()[i];
            while (node!= null){
                totalParcels++;
                ParcelRecord record = node.value;

                if (record.getStatus() == Parcel.Status.Dispatched && record.getDispatchTime() != null) {
                    dispatchedCount++;

                    int wait = record.getDispatchTime() - record.getArrivalTime();
                    totalWaitTime += wait;

                    if (maxWaitTime < wait) {
                        maxWaitTime = wait;
                        maxWaitParcelID = node.key;
                    }

                }

                node = node.next;
            }
        }

        double avgWaitTime = dispatchedCount > 0 ? totalWaitTime / dispatchedCount : 0;

        System.out.println("=== Final Report ===");
        System.out.println("== Simulation Overview ==");
        System.out.println("=> Total Ticks Executed:");
        System.out.println("=> Number of Parcels Generated:");
        System.out.println("== Parcel Statistics ==");
        System.out.println("=> Total Dispatched Parcels:");
        System.out.println("=> Total Returned Parcels:");
        System.out.println("=> Number of Parcels Still in Queue/BST/Stack at End:");
        System.out.println("== Destination Metrics ==");
        System.out.println("=> Number of Parcels per City:");
        System.out.println("=> Most Frequently Targeted Destination:");
        System.out.println("== Timing and Delay Metrics==");
        System.out.println("=> Average Parcel Processing Time:");
        System.out.println("=> Parcel With Longest Delay:");
        System.out.println("=> Number of Parcels Returned More Than Once:");
        System.out.println("=== Data Structure Statistics ===");
        System.out.println("=>  Maximum Queue Size Observed");
        System.out.println("=>  Maximum Stack Size Observed");
        System.out.println("=>  Final Height of BST");
        System.out.println("=>  Hash Table Load Factor");

    }

}
