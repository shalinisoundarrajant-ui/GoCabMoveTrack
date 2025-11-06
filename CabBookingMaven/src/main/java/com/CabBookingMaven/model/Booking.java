package com.CabBookingMaven.model;

import java.sql.Timestamp;

public class Booking {

    private int bookingId;
    private int customerId;
    private int cabId;
    private String pickupLocation;
    private String dropLocation;
    private int passengers;
    private double distance;
    private String status;      // Confirmed / Cancelled
    private double fare;
    private Timestamp bookingTime; // IMPORTANT

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getCabId() { return cabId; }
    public void setCabId(int cabId) { this.cabId = cabId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public int getPassengers() { return passengers; }
    public void setPassengers(int passengers) { this.passengers = passengers; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public Timestamp getBookingTime() { return bookingTime; }
    public void setBookingTime(Timestamp bookingTime) { this.bookingTime = bookingTime; }
    public double getAmount() {
        return this.fare;   // important
    }


    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
               ", Customer ID: " + customerId +
               ", Cab ID: " + cabId +
               ", Pickup: " + pickupLocation +
               ", Drop: " + dropLocation +
               ", Passengers: " + passengers +
               ", Distance: " + distance +
               ", Fare: ₹" + fare +
               ", Status: " + status +
               ", Booking Time: " + bookingTime;
    }
	
	
}
