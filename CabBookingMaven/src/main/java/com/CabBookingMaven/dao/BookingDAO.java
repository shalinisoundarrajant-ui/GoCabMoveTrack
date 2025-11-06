package com.CabBookingMaven.dao;

import com.CabBookingMaven.model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
	public static int NEW_BOOKING_ALERT = 0;
    public static int CANCEL_BOOKING_ALERT = 0;

    public int addBookingAndReturnId(Booking booking) {
        String sql = "INSERT INTO booking (customer_id, cab_id, pickup_location, drop_location, passengers, distance, fare, status, booking_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, booking.getCustomerId());
            ps.setInt(2, booking.getCabId());
            ps.setString(3, booking.getPickupLocation());
            ps.setString(4, booking.getDropLocation());
            ps.setInt(5, booking.getPassengers());
            ps.setDouble(6, booking.getDistance());
            ps.setDouble(7, booking.getFare());
            ps.setString(8, booking.getStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) 
                	{
                	 NEW_BOOKING_ALERT++; 
                	return rs.getInt(1);
                	}
            }

        } catch (Exception e) {
            System.err.println("❌ Error add booking: " + e.getMessage());
        }
        return -1;
    }

    // ✅ Fetch all bookings
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            System.err.println("❌ Error get all bookings: " + e.getMessage());
        }
        return list;
    }

    // ✅ Fetch by customer
    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE customer_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            System.err.println("❌ Error get customer bookings: " + e.getMessage());
        }
        return list; // always returns list, never null ✅
    }

    // ✅ get single booking
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM booking WHERE booking_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);

        } catch (Exception e) {
            System.err.println("❌ Error get booking: " + e.getMessage());
        }
        return null;
    }

    // ✅ cancel booking
    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE booking SET status='Cancelled' WHERE booking_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            int rows = ps.executeUpdate();

            if(rows > 0) {
                BookingDAO.CANCEL_BOOKING_ALERT++;
                return true;
            }

        } catch (Exception e) {
            System.err.println("❌ Error cancel booking: " + e.getMessage());
        }
        return false;
    }


    // ✅ check if cab currently booked
    public boolean isCabCurrentlyBooked(int cabId) {
        String sql = "SELECT COUNT(*) FROM booking WHERE cab_id=? AND status='Confirmed'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cabId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (Exception e) {
            System.err.println("❌ Error check cab booking: " + e.getMessage());
        }
        return false;
    }

    // mapper
    private Booking map(ResultSet rs) throws Exception {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setCabId(rs.getInt("cab_id"));
        b.setPickupLocation(rs.getString("pickup_location"));
        b.setDropLocation(rs.getString("drop_location"));
        b.setPassengers(rs.getInt("passengers"));
        b.setDistance(rs.getDouble("distance"));
        b.setFare(rs.getDouble("fare"));
        b.setStatus(rs.getString("status"));
        b.setBookingTime(rs.getTimestamp("booking_time")); // IMPORTANT
        return b;
    }
}
