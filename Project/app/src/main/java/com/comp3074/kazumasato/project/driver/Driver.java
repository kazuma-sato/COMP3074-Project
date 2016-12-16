package com.comp3074.kazumasato.project.driver;

// COMP3064  Group Assignment
// Due: Dec 12, 2016
// Due Extention: Thursday, December 15th, 2016
// Instructor: Ilir Dema
// Kazuma Sato 100 948 212 kazuma.sato@georgebrown.ca

import java.io.File;
import java.util.ArrayList;
import com.comp3074.kazumasato.project.models.Admin;
import com.comp3074.kazumasato.project.models.Flight;
import com.comp3074.kazumasato.project.models.User;


public class Driver {

    public static void uploadClientInfo(String path) {

        File file = new File(path);
        Admin.uploadClients(file);
    }

    
    public static void uploadFlightInfo(String path) {

        File file = new File(path);
        Admin.uploadFlights(file);
    }

    
    public static String getClient(String email) {

        User client = Admin.getClient(email);
        String message = String.format("%s,%s,%s,%s,%s,%s", client.getLastName(),
                client.getFirstName(), client.getEmail(), client.getAddress(),
                client.getCreditCardNumber(), client.getExpiryDate());
        return message;
    }

    
    public static String getFlights(String date, String origin, String destination) {

        ArrayList<Flight> flights = User.searchFlights(origin, destination, date);
        String message = "";
        for (Flight flight : flights) {
            message += String.format("%s,%s,%s,%s,%s,%s,%.2f\n", flight.getFlightnum(),
                    flight.getDepartureDateTime(), flight.getArrivalDateTime(), flight.getAirline(),
                    flight.getOrigin(), flight.getDestination(), flight.getCost());
        }
        return message;
    }

    
    public static String getItineraries(String date, String origin, String destination) {

        Admin temp = new Admin(null, null, "temp@admin.com", null, null, null, null);
        String itineraries = temp.getItineraries(date, origin, destination);
        Admin.removeAdmin(temp);
        return itineraries;
    }

    
    public static String getItinerariesSortedByCost(String date, String origin, String destination) {

        Admin temp = new Admin(null, null, "temp@admin.com", null, null, null, null);
        String itineraries = temp.getItinerariesSortedByCost(date, origin, destination);
        Admin.removeAdmin(temp);
        return itineraries;
    }

    
    public static String getItinerariesSortedByTime(String date, String origin, String destination) {
        
        Admin temp = new Admin(null, null, "temp@admin.com", null, null, null, null);
        String itineraries = temp.getItinerariesSortedByTime(date, origin, destination);
        Admin.removeAdmin(temp);
        return itineraries;
    }
}
