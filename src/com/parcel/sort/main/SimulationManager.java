package com.parcel.sort.main;

import com.parcel.sort.data_structures.*;
import com.parcel.sort.entities.Parcel;
import com.parcel.sort.model.ParcelRecord;
import com.parcel.sort.utils.ReportGenerator;

import java.util.Random;

public class SimulationManager {

    //tick initalization
    private int currentTick;
    private ConfigReader configReader;
    private ArrivalBuffer arrivalBuffer;
    private ReturnStack returnStack;
    private DestinationSorter destinationSorter;
    private ParcelTracker parcelTracker;
    private TerminalRotator terminalRotator;
    private Logger logger;
    private ReportGenerator reportGenerator;

    private Random random;

    public SimulationManager() {
        this.configReader = new ConfigReader();
        this.arrivalBuffer = new ArrivalBuffer();
        this.returnStack = new ReturnStack();
        this.destinationSorter = new DestinationSorter();
        this.parcelTracker = new ParcelTracker(configReader.getQueueCapacity());
        this.terminalRotator = new TerminalRotator(configReader.getCityList());
        this.logger = new Logger("report.txt");
        this.reportGenerator = new ReportGenerator();
        this.random = new Random();
        this.currentTick = 0;
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

            // enqueue and track
            arrivalBuffer.enqueue(parcel, currentTick);
            parcelTracker.insert(parcel.getParcelID(), record);

            // log arrival
            logger.logParcelArrival(parcel);
        }

        logger.logQueueSize(arrivalBuffer.size());
    }


    //queue processing

    //dispatch evaluation

    //returnstack reprocessing

    //terminal rotation

    //tick summary logging

}
