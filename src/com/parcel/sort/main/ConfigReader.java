package com.parcel.sort.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConfigReader {
    private int maxTicks = 300;
    private int queueCapacity = 30;
    private int terminalRotationInterval = 5;
    private int parcelPerTickMin = 1;
    private int parcelPerTickMax = 3;
    private double misroutingRate = 0.1;
    private String[] cityList = {"Istanbul", "Ankara", "Izmir", "Bursa", "Antalya"};

    public ConfigReader() {
        readConfigFile();
    }

    private void readConfigFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("MAX_TICKS=")) {
                    maxTicks = Integer.parseInt(line.split("=")[1].trim());
                } else if (line.startsWith("QUEUE_CAPACITY=")) {
                    queueCapacity = Integer.parseInt(line.split("=")[1].trim());
                } else if (line.startsWith("TERMINAL_ROTATION_INTERVAL=")) {
                    terminalRotationInterval = Integer.parseInt(line.split("=")[1].trim());
                } else if (line.startsWith("PARCEL_PER_TICK_MIN=")) {
                    parcelPerTickMin = Integer.parseInt(line.split("=")[1].trim());
                } else if (line.startsWith("PARCEL_PER_TICK_MAX=")) {
                    parcelPerTickMax = Integer.parseInt(line.split("=")[1].trim());
                } else if (line.startsWith("MISROUTING_RATE=")) {
                    misroutingRate = Double.parseDouble(line.split("=")[1].trim());
                } else if (line.startsWith("CITY_LIST=")) {
                    cityList = line.split("=")[1].trim().split("\\s*,\\s*"); // the number of spaces don't matter
                }
            }
        } catch (IOException e) {
            System.out.println("Config file could not be read. Default values will be used.");
        }
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public int getTerminalRotationInterval() {
        return terminalRotationInterval;
    }

    public int getParcelPerTickMin() {
        return parcelPerTickMin;
    }

    public int getParcelPerTickMax() {
        return parcelPerTickMax;
    }

    public double getMisroutingRate() {
        return misroutingRate;
    }

    public String[] getCityList() {
        return cityList;
    }
}
