package com.comp3074.kazumasato.project.models;

import java.io.Serializable;
import java.util.Calendar;

public class Flight implements Serializable {

    private String flightnum;
    private String departureDateTime;
    private String arrivalDateTime;
    private String airline;
    private String origin;
    private String destination;
    private double traveltime;
    private double cost;
    private int numSeats;
    private int bookedSeats;

    public Flight(String flightnum, String departureDateTime, String arrivalDateTime, String airline,
                  String origin, String destination, double cost, int numSeats) {
        this.flightnum = flightnum;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.cost = cost;
        this.numSeats = numSeats;
        this.bookedSeats = 0;

        Calendar date1 = createNewCalendar(departureDateTime);
        Calendar date2 = createNewCalendar(arrivalDateTime);
        double timeDifference = date2.getTimeInMillis() - date1.getTimeInMillis();
        this.traveltime = timeDifference / (1000 * 60 * 60);
    }

    public String getFlightnum() {
        return flightnum;
    }
    public void setFlightnum(String flightnum) {
        this.flightnum = flightnum;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }
    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }
    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getAirline() {
        return airline;
    }
    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }


    public double getTraveltime() {
        return traveltime;
    }
    public void setTraveltime(double traveltime) {
        this.traveltime = traveltime;
    }

    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
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

    public double getWaitTime(Flight flight2) {

        Calendar date1 = createNewCalendar(arrivalDateTime);
        Calendar date2 = createNewCalendar(flight2.getDepartureDateTime());
        double timeDifference = date2.getTimeInMillis() - date1.getTimeInMillis();

        return timeDifference / (1000 * 60 * 60);
    }


    protected boolean isfull() {

        return this.numSeats == this.bookedSeats;
    }

    public boolean bookSeat() {

        if (isfull()) {
            return false;
        } else {
            this.bookedSeats += 1;
            return true;
        }
    }

    public void cancelSeat() {
        this.bookedSeats = this.bookedSeats - 1;
    }

    @Override
    public String toString() {
        String message = "";
        message += "Flight Number: " + this.getFlightnum() + "\n";
        message += "Departure Date and Time: " + this.getDepartureDateTime() + "\n";
        message += "Arrival Date and Time: " + this.getArrivalDateTime() + "\n";
        message += "Airline: " + this.getAirline() + "\n";
        message += "Origin: " + this.getOrigin() + "\n";
        message += "Destination: " + this.getDestination() + "\n";
        message += "Price: " + String.valueOf(this.getCost()) + "\n";
        message += "Number of Seats Available: " + String.valueOf(this.getNumSeats()) + "\n\n";
        return message;
    }

    public boolean equals(Flight flight) {
        return this.getFlightnum().equals(flight.getFlightnum());
    }
}
