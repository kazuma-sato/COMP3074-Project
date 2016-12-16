package com.comp3074.kazumasato.project.db;

// COMP3064  Group Assignment
// Due: Dec 12, 2016
// Due Extention: Thursday, December 15th, 2016
// Instructor: Ilir Dema
// Kazuma Sato 100 948 212 kazuma.sato@georgebrown.ca

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.comp3074.kazumasato.project.models.*;

public class FlightDatabase implements Database<Flight> {

    private ArrayList<Flight> flights;
    private Gson gson = new Gson();
    private Type flightType = new TypeToken<ArrayList<Flight>>() {
    }.getType();
    private static Context context;

    private File getFlightsFile() {

        File userData = FlightDatabase.context.getApplicationContext().getDir("databases",
                Context.MODE_PRIVATE);
        return new File(userData, "flights.json");
    }

    public FlightDatabase() {

        this(FlightDatabase.context);
    }

    
    public FlightDatabase(Context context) {

        FlightDatabase.context = context;
        File flightsFile = this.getFlightsFile();

        if (!flightsFile.exists()) {
            this.flights = new ArrayList<>();
            this.save();
        } else {
            this.load();
        }
    }

    
    private int findFlightIndex(Flight flight) {

        int index = 0;

        if (this.flights.isEmpty()) {
            index = -1;
        } else {
            for (Flight currFlight : this.flights) {
                if (flight.getFlightnum().equals(currFlight.getFlightnum())) {
                    break;
                } else {
                    index += 1;
                }
            }
        }

        if (index >= this.flights.size()) {
            index = -1;
            return index;
        } else {
            return index;
        }
    }

    
    @Override
    public void addItem(Flight flight) {

        int index = findFlightIndex(flight);

        if (index < 0) {
            this.flights.add(flight);
        } else {
            if (index < this.flights.size()) {
                this.flights.set(index, flight);
            }
        }
    }

    
    @Override
    public boolean removeItem(Flight flight) {

        int index = findFlightIndex(flight);

        if (index < 0) {
            return false;
        } else {
            this.flights.remove(index);
            return true;
        }
    }

    
    @Override
    public Flight getItem(int index) {

        if (index < 0 || index >= this.flights.size()) {
            return null;
        } else {
            return this.flights.get(index);
        }
    }

    
    public Flight getFlight(String flightNum) {

        Flight flight = null;

        for (Flight currFlight : this.flights) {
            if (flightNum.equals(currFlight.getFlightnum())) {
                flight = currFlight;
                return flight;
            }
        }
        return null;
    }

    
    public ArrayList<Flight> getFlights(String origin, String destination, String departureDate) {
       
        ArrayList<Flight> flights = new ArrayList<Flight>();

        for (Flight flight : this.flights) {
            String date = flight.getDepartureDateTime().substring(0, 10);
            if (origin.equals(flight.getOrigin()) && 
                    destination.equals(flight.getDestination()) &&
                    departureDate.equals(date)) {
                flights.add(flight);
            }
        }
        return flights;
    }

    
    public ArrayList<Flight> getFlightsOfOrigin(String origin) {

        ArrayList<Flight> flights = new ArrayList<Flight>();
        for (Flight flight : this.flights) {
            if (origin.equals(flight.getOrigin())) {
                flights.add(flight);
            }
        }
        return flights;
    }

    
    public ArrayList<Flight> getAllFlights() {

        return this.flights;
    }

    
    @Override
    public boolean contains(String flightNum) {

        boolean result = false;
        for (Flight flight : this.flights) {
            if (flightNum.equals(flight.getFlightnum())) {
                result = true;
                break;
            }
        }
        return result;
    }

    
    public boolean isEmpty() {

        return this.flights.isEmpty();
    }

    
    @Override
    public int size() {

        return this.flights.size();
    }

    
    @Override
    public void save() {

        try {
            File flightsFile = this.getFlightsFile();
            FileWriter writer = new FileWriter(flightsFile);
            gson.toJson(this.flights, this.flightType, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public void load() {

        try {
            File flightsFile = this.getFlightsFile();
            BufferedReader reader = new BufferedReader(new FileReader(flightsFile));
            this.flights = gson.fromJson(reader, this.flightType);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public String toString() {

        String message = "[Flight Database]\n";
        
        for (Flight flight : this.flights) {
            message += flight.toString() + "\n";
        }
        return message;
    }
}
