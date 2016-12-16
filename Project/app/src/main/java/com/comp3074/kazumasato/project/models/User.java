package com.comp3074.kazumasato.project.models;

import com.comp3074.kazumasato.project.db.FlightDatabase;
import com.comp3074.kazumasato.project.db.UserDatabase;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {
    private ArrayList<Itinerary> bookedItineraries;
    private ArrayList<Itinerary> itineraryList;
    protected static UserDatabase userDatabase = new UserDatabase();
    protected static FlightDatabase flightDatabase = new FlightDatabase();
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String address;
    private String creditCardNumber;
    private String expiryDate;
    private boolean isAdmin;

    
    public User(String lastName, String firstName, String email, String password, String address,
                String creditCardNumber, String expiryDate, boolean isAdmin) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
        this.bookedItineraries = new ArrayList<Itinerary>();
        this.itineraryList = new ArrayList<Itinerary>();
        this.isAdmin = isAdmin;
        User.userDatabase.addItem(this);
        User.userDatabase.save();
    }

    
    public String getLastName() {
        return lastName;
    }

    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
    public String getFirstName() {
        return firstName;
    }

    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword(){ return password; }
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getAddress() {
        return address;
    }

    
    public void setAddress(String address) {
        this.address = address;
    }

    
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    
    public String getExpiryDate() {
        return expiryDate;
    }

    
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    
    public boolean isAdmin() {
        return this.isAdmin;
    }

    
    public static Flight getFlight(String flightNum) {
        return User.flightDatabase.getFlight(flightNum);
    }

    
    public static ArrayList<Flight> searchFlights(String origin, String destination,
                                                  String departureDate) {
        return User.flightDatabase.getFlights(origin, destination, departureDate);
    }

    
    public void clearItineraries() {
        this.itineraryList.clear();
    }

    
    public void clearBookedItineraries() {
        this.bookedItineraries.clear();
    }

    
    public ArrayList<Itinerary> getItineraryListAsList() {
        return itineraryList;
    }

    
    public String getItineraryList() {
        String message = "";
        for (int i = 0; i < itineraryList.size(); i++) {
            message += itineraryList.get(i).toString();
            if (i != itineraryList.size() - 1) {
                message += "\n";
            }
        }
        return message;
    }

    
    public ArrayList<Itinerary> getBookedItineraryListAsList() {
        return bookedItineraries;
    }

    
    private ArrayList<ArrayList<Flight>> search(String origin, String destination,
                                                ArrayList<String> locations) {
        ArrayList<Flight> allOrigins = User.flightDatabase.getFlightsOfOrigin(origin);
        if (allOrigins.size() == 0) {
            return new ArrayList<ArrayList<Flight>>();
        }
        ArrayList<ArrayList<Flight>> allFlights = new ArrayList<ArrayList<Flight>>();
        for (Flight flight : allOrigins) {
            locations.add(origin);
            if (flight.getDestination().equals(destination)) {
                ArrayList<Flight> newFlight = new ArrayList<Flight>();
                newFlight.add(flight);
                allFlights.add(newFlight);
            } else if ((!locations.contains(flight.getDestination()))) {
                ArrayList<ArrayList<Flight>> listofsubFlights = search(flight.getDestination(),
                        destination, locations);
                for (ArrayList<Flight> subFlights : listofsubFlights) {
                    subFlights.add(0, flight);
                }
                allFlights.addAll(listofsubFlights);
            }
            locations.remove(locations.size() - 1);
        }
        return allFlights;
    }

    
    private void createItineraries(String date, String origin, String destination) {
        ArrayList<ArrayList<Flight>> allflights = search(origin, destination, new ArrayList<String>());
        for (ArrayList<Flight> flights : allflights) {
            Itinerary itinerary = new Itinerary(flights);
            if (!itinerary.is_empty() && itinerary.getFirstDepartureDate().equals(date)
                    && itinerary.hasValidWaitTimes() && itinerary.flightsHaveRoom()) {
                this.itineraryList.add(itinerary);
            }
        }

    }

    
    public String getItineraries(String date, String origin, String destination) {
        createItineraries(date, origin, destination);
        return getItineraryList();
    }

    
    private ArrayList<Itinerary> sortByTravelTime(ArrayList<Itinerary> itineraryList) {
        ArrayList<Itinerary> newItin = new ArrayList<Itinerary>();
        for (Itinerary itin : itineraryList) {
            if (newItin.isEmpty()) {
                newItin.add(itin);
            } else {
                boolean added = false;
                for (Itinerary tempItin : newItin) {
                    if (itin.totalTravelTime() < tempItin.totalTravelTime()) {
                        int index = newItin.indexOf(tempItin);
                        newItin.add(index, itin);
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    newItin.add(itin);
                }
            }
        }
        return newItin;
    }

    
    public String getItinerariesSortedByTime(String date, String origin, String destination) {
        createItineraries(date, origin, destination);
        this.itineraryList = sortByTravelTime(itineraryList);
        return getItineraryList();
    }

    
    public ArrayList<Itinerary> sortByTime() {
        this.itineraryList = sortByTravelTime(itineraryList);
        return itineraryList;
    }

    
    private ArrayList<Itinerary> sortByTotalCost(ArrayList<Itinerary> itineraryList) {
        ArrayList<Itinerary> newItin = new ArrayList<Itinerary>();
        for (Itinerary itin : itineraryList) {
            if (newItin.isEmpty()) {
                newItin.add(itin);
            } else {
                boolean added = false;
                for (Itinerary tempItin : newItin) {
                    if (itin.getTotalCost() < tempItin.getTotalCost()) {
                        int index = newItin.indexOf(tempItin);
                        newItin.add(index, itin);
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    newItin.add(itin);
                }
            }
        }
        return newItin;
    }

    
    public String getItinerariesSortedByCost(String date, String origin, String destination) {
        createItineraries(date, origin, destination);
        this.itineraryList = sortByTotalCost(itineraryList);
        return getItineraryList();
    }

    
    public ArrayList<Itinerary> sortByCost() {
        this.itineraryList = sortByTotalCost(itineraryList);
        return itineraryList;
    }

    
    public boolean isBooked(Itinerary newItin) {
        for (Itinerary itinerary : this.bookedItineraries) {
            if (newItin.equals(itinerary)) {
                return true;
            }
        }
        return false;
    }

    
    public boolean bookItinerary(Itinerary newItin) {
        if (!(isBooked(newItin))) {
            if (newItin.flightsHaveRoom()) {
                this.bookedItineraries.add(newItin);
                newItin.bookFlights();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    
    public void cancelItinerary(Itinerary newItin) {
        for (Itinerary itinerary : this.bookedItineraries) {
            if (itinerary.equals(newItin)) {
                this.bookedItineraries.remove(itinerary);
                itinerary.cancelFlights();
                break;
            }
        }
    }

    
    public ArrayList<Itinerary> viewBookedItineraries() {
        return this.bookedItineraries;
    }

    
    @Override
    public String toString() {
        return String.format("%s, %s\n%s\n%s\n%s, %s\n", this.getLastName(),
                this.getFirstName(), this.getEmail(), this.getAddress(), this.getCreditCardNumber(),
                this.getExpiryDate());
    }
}
