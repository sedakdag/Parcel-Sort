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

    //all components
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
        this.logger = new Logger("simulation_log.txt");
        this.arrivalBuffer = new ArrivalBuffer();
        this.returnStack = new ReturnStack();
        this.destinationSorter = new DestinationSorter();
        this.parcelTracker = new ParcelTracker(configReader.getQueueCapacity());
        this.terminalRotator = new TerminalRotator(configReader.getCityList(), this.logger);
        this.random = new Random();
        this.currentTick = 0;
        this.reprocessInterval = 3;
        this.generatedParcels = 0;
        this.reportGenerator = new ReportGenerator(parcelTracker, destinationSorter, this, arrivalBuffer, returnStack);
    }

    //parcel generation
    public void generateParcels() {
        int minParcels = configReader.getParcelPerTickMin();
        int maxParcels = configReader.getParcelPerTickMax();

        // ensure maxParcels is not less than minParcels
        if (maxParcels < minParcels) {
            maxParcels = minParcels;
        }

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
    public void sortParcels(){
        while (!arrivalBuffer.isEmpty()) {
            Parcel parcel = arrivalBuffer.dequeue();
            if(parcel != null){
                destinationSorter.insertParcel(parcel);
                parcelTracker.updateStatus(parcel.getParcelID(), Parcel.Status.Sorted, null);
                //Logging the status
                logger.logStatusChange(parcel.getParcelID(), Parcel.Status.Sorted);
                logger.logBSTInsertion(parcel);
            }
        }
    }

    //dispatch evaluation
    public void evaluateDispatch(){
        String currentCity = terminalRotator.getActiveTerminal();

        if (destinationSorter.countCityParcels(currentCity) > 0) {
            DestinationSorter.ParcelQueue queue = destinationSorter.getCityParcels(currentCity);
            Parcel parcel = queue.dequeue();
            double chance = Math.random();

            if (chance < configReader.getMisroutingRate()) {
                returnStack.push(parcel);
                parcelTracker.updateStatus(parcel.getParcelID(), Parcel.Status.Returned, null);
                //logging the status
                logger.logStatusChange(parcel.getParcelID(), Parcel.Status.Returned);
                parcelTracker.incrementReturnCount(parcel.getParcelID());

                ParcelRecord record = parcelTracker.get(parcel.getParcelID());
                int returnCount = (record != null) ? record.getReturnCount() : 0;
                logger.logReturn(parcel, returnCount);
            }
            else {
                parcelTracker.updateStatus(parcel.getParcelID(), Parcel.Status.Dispatched, currentTick);
                logger.logStatusChange(parcel.getParcelID(), Parcel.Status.Dispatched);
                destinationSorter.removeParcel(currentCity, parcel.getParcelID());
                logger.logDispatchSuccess(parcel, currentCity);
            }
        }
    }

    //returnstack reprocessing
    public void reprocessReturnedParcels(int currentTick) {
        if (currentTick % reprocessInterval != 0) {
            return;
        }
        int count = 0;

        while (!returnStack.isEmpty()) {
            Parcel parcel = returnStack.pop();
            if(parcel != null){
                destinationSorter.insertParcel(parcel);
                parcelTracker.updateStatus(parcel.getParcelID(), Parcel.Status.Sorted, null);
                logger.logStatusChange(parcel.getParcelID(), Parcel.Status.Sorted);
                logger.logReturn(parcel, count);
                count++;
            }
        }

    }

    //terminal rotation
    public void rotateTerminal(){
        if (currentTick > 0 && currentTick % configReader.getTerminalRotationInterval() == 0) {
            terminalRotator.advanceTerminal();
            logger.logTerminalChange(terminalRotator.getActiveTerminal());
        }
    }

    //tick summary logging
    public void logTickSummary(int currentTick) {
        System.out.println("\n--- Tick " + currentTick + " Summary ---");
        System.out.println("  Arrival Buffer size: " + arrivalBuffer.size());
        System.out.println("  Return Stack size: " + returnStack.size());
        System.out.println("  Active terminal: " + terminalRotator.getActiveTerminal());
        System.out.println("  Total Dispatched parcels: " + parcelTracker.getDispatchedCount());
        System.out.println("  Total Returned parcels (cumulative): " + parcelTracker.getReturnedCount());
        System.out.println("---------------------------\n");
    }

    public int getSimulationDuration() {
        return configReader.getMaxTicks();
    }

    public void generateSimulationReport() {
        this.reportGenerator.generateReport();
    }

    public void closeLoggers() {
        this.logger.close();
    }

    // Main method: Entry point for running the simulation
    public static void main(String[] args) {
        SimulationManager simulation = new SimulationManager();
        int simulationDuration = simulation.getSimulationDuration(); // Get simulation duration from config

        System.out.println("Starting Parcel Sort Simulation for " + simulationDuration + " ticks.");

        // Main simulation loop, runs for the configured number of ticks
        for (simulation.currentTick = 0; simulation.currentTick < simulationDuration; simulation.currentTick++) {
            // Log tick start (including console and file)
            simulation.logger.logTickStart(simulation.currentTick);

            // Execute simulation steps for the current tick
            simulation.generateParcels();
            simulation.sortParcels();
            simulation.evaluateDispatch();
            simulation.reprocessReturnedParcels(simulation.currentTick);
            simulation.rotateTerminal();
            simulation.logTickSummary(simulation.currentTick); // Print summary to console

            // Optional: Add a small delay for better readability in console output
            try {
                Thread.sleep(50); // Sleep for 50 milliseconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                System.err.println("Simulation interrupted: " + e.getMessage());
            }
        }

        // After the simulation loop finishes
        System.out.println("\nSimulation Finished.");
        simulation.generateSimulationReport(); // Generate the final report
        simulation.closeLoggers(); // Close all loggers
        System.out.println("Final report generated and loggers closed.");
    }
}
