package com.comp3074.kazumasato.project.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


public class Itinerary implements Serializable {

    private ArrayList<Flight> flights;

    
    public Itinerary() {
        this.flights = new ArrayList<Flight>();
    }

    
    public Itinerary(ArrayList<Flight> flights) {
        this.flights = new ArrayList<Flight>();
        this.flights.addAll(flights);
    }

    
    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    
    public int numFlights() {
        return flights.size();
    }

    
    public double getTotalCost() {
        double costs = 0;
        for (Flight flight : flights) {
            costs += flight.getCost();
        }
        return costs;
    }

    
    public double totalTravelTime() {
        Calendar date1 = createNewCalendar(getFirstDepartureDateTime());
        Calendar date2 = createNewCalendar(getLastArrivalDateTime());
        double timeDifference = date2.getTimeInMillis() - date1.getTimeInMillis();
        return timeDifference / (1000 * 60 * 60);

    }

    
    public String getStartOrigin() {
        Flight firstFlight = flights.get(0);
        return firstFlight.getOrigin();
    }

    
    public String getFinalDestination() {
        Flight lastFlight = flights.get(flights.size() - 1);
        return lastFlight.getDestination();
    }

    
    public String getFirstDepartureDateTime() {
        Flight firstFlight = flights.get(0);
        return firstFlight.getDepartureDateTime();
    }

    
    public String getLastArrivalDateTime() {
        Flight lastFlight = flights.get(flights.size() - 1);
        return lastFlight.getArrivalDateTime();
    }

    
    public String getFirstDepartureDate() {
        Flight firstFlight = flights.get(0);
        return firstFlight.getDepartureDateTime().split(" ")[0];
    }

    
    public String getLastArrivalDate() {
        Flight lastFlight = flights.get(flights.size() - 1);
        return lastFlight.getArrivalDateTime().split(" ")[0];
    }

    
    public boolean is_empty() {
        return (this.flights.size() == 0);
    }

    
    public ArrayList<Flight> getFlights() {
        return this.flights;
    }

    
    private Calendar createNewCalendar(String dateAndTime) {
        Calendar calendar = Calendar.getInstance();
        String date = dateAndTime.split(" ")[0];
        String time = dateAndTime.split(" ")[1];
        int year = Integer.valueOf(date.split("-")[0]);
        int month = Integer.valueOf(date.split("-")[1]) - 1;
        int day = Integer.valueOf(date.split("-")[2]);
        int hour = Integer.valueOf(time.split(":")[0]);
        int min = Integer.valueOf(time.split(":")[1]);
        calendar.set(year, month, day, hour, min);
        return calendar;
    }

    
    protected boolean hasValidWaitTimes() {
        for (int i = 0; i < this.flights.size() - 1; i++) {
            Flight flight1 = this.flights.get(i);
            Flight flight2 = this.flights.get(i + 1);
            double waittime = flight1.getWaitTime(flight2);
            if ((waittime > 6) || (waittime < 0.5)) {
                return false;
            }
        }
        return true;
    }

    
    protected boolean flightsHaveRoom() {
        for (Flight flight : this.flights) {
            if (flight.isfull()) {
                return false;
            }
        }
        return true;
    }

    
    public void bookFlights() {
        for (Flight flight : this.flights) {
            flight.bookSeat();
        }
    }

    
    public void cancelFlights() {
        for (Flight flight : this.flights) {
            flight.cancelSeat();
        }
    }

    
    public String toString() {
        String message = "";
        for (Flight flight : this.flights) {
            message += String.format("%s,%s,%s,%s,%s,%s\n", flight.getFlightnum(),
                    flight.getDepartureDateTime(), flight.getArrivalDateTime(), flight.getAirline(),
                    flight.getOrigin(), flight.getDestination());
        }
        Calendar date1 = createNewCalendar(getFirstDepartureDateTime());
        Calendar date2 = createNewCalendar(getLastArrivalDateTime());
        long timeDifference = date2.getTimeInMillis() - date1.getTimeInMillis();
        long diffMinutes = timeDifference / (60 * 1000) % 60;
        long diffHours = timeDifference / (60 * 60 * 1000);

        String hours = String.valueOf(diffHours);
        String minutes = String.valueOf(diffMinutes);
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        String time = hours + ":" + minutes;
        message += String.format("%.2f\n%s", this.getTotalCost(), time);

        return message;
    }

    
    public boolean equals(Itinerary itinerary) {
        if (this.flights.size() != itinerary.getFlights().size()) {
            return false;
        } else {
            for (int i = 0; i < this.flights.size(); i++) {
                if (!this.flights.get(i).equals(itinerary.getFlights().get(i))) {
                    return false;
                }
            }
            return true;
        }
    }
}
