package com.parcel.sort.main;

import com.parcel.sort.data_structures.*;
import com.parcel.sort.entities.Parcel;
import com.parcel.sort.model.ParcelRecord;
import com.parcel.sort.utils.ReportGenerator;


import java.util.Random;

public class SimulationManager {

    //tick initalization
    public int currentTick;
    public int generatedParcels;
    private ConfigReader configReader;
    private ArrivalBuffer arrivalBuffer;
    private ReturnStack returnStack;
    private DestinationSorter destinationSorter;
    private ParcelTracker parcelTracker;
    private TerminalRotator terminalRotator;
    private Logger logger;
    private ReportGenerator reportGenerator;
    private int reprocessInterval;

    private Random random;

    public SimulationManager() {
        this.configReader = new ConfigReader();
        this.arrivalBuffer = new ArrivalBuffer();
        this.returnStack = new ReturnStack();
        this.destinationSorter = new DestinationSorter();
        this.parcelTracker = new ParcelTracker(configReader.getQueueCapacity());
        this.terminalRotator = new TerminalRotator(configReader.getCityList());
        this.logger = new Logger("report.txt");
        this.reportGenerator = new ReportGenerator( parcelTracker, destinationSorter);
        this.random = new Random();
        this.currentTick = 0;
        this.reprocessInterval = 3;
        this.generatedParcels = 0;
    }

    //parcel generation
    private void generateParcels() {
        int minParcels = configReader.getParcelPerTickMin();
        int maxParcels = configReader.getParcelPerTickMax();
        int parcelsPerTick = random.nextInt(maxParcels - minParcels + 1) + minParcels;

        for (int i = 0; i < parcelsPerTick; i++) {
            Parcel parcel = Parcel.generateRandomParcel(currentTick, configReader.getCityList());

            // create corresponding record
            ParcelRecord record = new ParcelRecord(
                    parcel.getParcelID(),
                    currentTick,
                    parcel.getDestinationCity(),
                    parcel.getPriority(),
                    parcel.getSize()
            );
            generatedParcels++;

            // enqueue and track
            arrivalBuffer.enqueue(parcel, currentTick);
            parcelTracker.insert(parcel.getParcelID(), record);

            // log arrival
            logger.logParcelArrival(parcel);
        }

        logger.logQueueSize(arrivalBuffer.size());
    }


    //queue processing

    private void sortParcels(){
        while (!arrivalBuffer.isEmpty()) {
            Parcel parcel = arrivalBuffer.dequeue();
            destinationSorter.insertParcel(parcel);
            parcelTracker.updateStatus(parcel.getParcelID(), Parcel.Status.Sorted, null);
            logger.logBSTInsertion(parcel);
        }
    }

    //dispatch evaluation

    private void evaluateDispatch(){
        String currentCity = terminalRotator.getActiveTerminal();

        if (destinationSorter.countCityParcels(currentCity) > 0) {
            DestinationSorter.ParcelQueue queue = destinationSorter.getCityParcels(currentCity);
            Parcel parcel = queue.dequeue();
            double chance = Math.random();

            if (chance < configReader.getMisroutingRate()) {
                returnStack.push(parcel);
                parcelTracker.updateStatus(parcel.getParcelID(), Parcel.Status.Returned, null);
                parcelTracker.incrementReturnCount(parcel.getParcelID());

                ParcelRecord record = parcelTracker.get(parcel.getParcelID());
                int returnCount = record.getReturnCount();
                logger.logReturn(parcel, returnCount);
            }
            else {
                parcelTracker.updateStatus(parcel.getParcelID(), Parcel.Status.Dispatched, currentTick);
                destinationSorter.removeParcel(currentCity, parcel.getParcelID());
                logger.logDispatchSuccess(parcel, currentCity);
            }
        }
    }


    //returnstack reprocessing

    private void reprocessReturnedParcels(int currentTick) {
        if (currentTick % reprocessInterval != 0) {
            return;
        }
        int count = 0;

        while (!returnStack.isEmpty() && count < reprocessInterval) { //limit??
            Parcel parcel = returnStack.pop();

            destinationSorter.insertParcel(parcel);
            parcelTracker.updateStatus(parcel.getParcelID(), Parcel.Status.Sorted, null);
            logger.logReturn(parcel, count);
            count++;
        }

    }

    //terminal rotation
    private void rotateTerminal(){
        if (currentTick % configReader.getTerminalRotationInterval() == 0) {
            terminalRotator.advanceTerminal();
            logger.logTerminalChange(terminalRotator.getActiveTerminal());
        }
    }



    //tick summary logging
    private void logTickSummary(int currentTick) {

        System.out.println("Tick" + currentTick + "Summary: " );
        System.out.println("Queue size: " + arrivalBuffer.size());
        System.out.println("Stack size: " + returnStack.size());
        System.out.println("Active terminal: " + terminalRotator.getActiveTerminal());
        System.out.println("Dispatched parcels: " + parcelTracker.getDispatchedCount());
        System.out.println("Returned parcels: " + parcelTracker.getReturnedCount());
    }
}
