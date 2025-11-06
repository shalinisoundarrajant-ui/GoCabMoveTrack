package com.CabBookingMaven.Service;

import com.CabBookingMaven.dao.BookingDAO;
import com.CabBookingMaven.model.Booking;
import com.CabBookingMaven.model.Cab;
import com.CabBookingMaven.model.Customer;
import com.CabBookingMaven.model.Payment;

import java.util.List;
import java.util.Scanner;

public class BookingService {

    private BookingDAO bookingDAO = new BookingDAO();
    private CabService cabService = new CabService();
    private PaymentService paymentService = new PaymentService();
    private Scanner sc = new Scanner(System.in);

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    public boolean bookCab(Customer customer, int cabId, String pickup, String drop, int passengers, double distance) {

        Cab cab = cabService.getCabById(cabId);
        if (cab != null && cab.isAvailability()) {

            if (passengers > cab.getSeatCapacity()) {
                System.out.println(RED + "❌ Exceeds seat capacity!" + RESET);
                return false;
            }

            double totalFare = distance * cab.getFarePerKm();
            System.out.println(YELLOW + "💰 Total Fare: ₹" + totalFare + RESET);

            Payment payment = new Payment();
            payment.setAmount(totalFare);

            if (!processPayment(customer, payment, totalFare)) {
                System.out.println(RED + "❌ Payment failed!" + RESET);
                return false;
            }

            Booking booking = new Booking();
            booking.setCustomerId(customer.getCustomerId());
            booking.setCabId(cabId);
            booking.setPickupLocation(pickup);
            booking.setDropLocation(drop);
            booking.setPassengers(passengers);
            booking.setDistance(distance);
            booking.setFare(totalFare);
            booking.setStatus("Confirmed");

            int bookingId = bookingDAO.addBookingAndReturnId(booking);

            if (bookingId > 0) {

                cabService.updateCabAvailability(cabId, false);

                System.out.println(GREEN + "✅ Booking Successful! (Booking ID: " + bookingId + ")" + RESET);

                payment.setBookingId(bookingId);
                paymentService.makePayment(payment);

                return true;
            } else {
                System.out.println(RED + "❌ Booking failed." + RESET);
            }

        } else {
            System.out.println(RED + "❌ Cab not available." + RESET);
        }

        return false;
    }

    public void viewAllBookings() {
        List<Booking> list = bookingDAO.getAllBookings();
        if (list.isEmpty()) System.out.println(YELLOW + "⚠️ No bookings found." + RESET);
        else list.forEach(System.out::println);
    }

    public void viewCustomerBookings(int customerId) {
        List<Booking> list = bookingDAO.getBookingsByCustomerId(customerId);
        if (list.isEmpty()) System.out.println(YELLOW + "⚠️ No bookings found." + RESET);
        else list.forEach(System.out::println);
    }

    public Booking getBookingById(int bookingId) {
        return bookingDAO.getBookingById(bookingId);
    }

    private boolean processPayment(Customer c, Payment p, double fare) {
        System.out.println(PURPLE + "\n--- 💳 Select Payment Method ---" + RESET);
        System.out.println(YELLOW + "1. Cash\n2. GPay" + RESET);
        System.out.print(CYAN + "Enter choice: " + RESET);
        int ch = sc.nextInt();
        sc.nextLine();

        if (ch == 1) return handleCash(p, fare);
        else if (ch == 2) return handleGPay(c, p, fare);
        else {
            System.out.println(RED + "❌ Invalid choice!" + RESET);
            return false;
        }
    }

    private boolean handleCash(Payment p, double fare) {
        p.setPaymentMethod("Cash");
        System.out.print(CYAN + "Enter cash amount: ₹" + RESET);
        double cash = sc.nextDouble();
        sc.nextLine();

        if (cash >= fare) {
            p.setPaymentStatus("Success");
            double bal = cash - fare;
            System.out.println(GREEN + "💵 Cash payment successful!" + RESET);
            if (bal > 0) System.out.println(YELLOW + "💸 Change: ₹" + bal + RESET);
            return true;
        } else {
            p.setPaymentStatus("Failed");
            System.out.println(RED + "❌ Insufficient cash!" + RESET);
            return false;
        }
    }

    private boolean handleGPay(Customer c, Payment p, double fare) {
        p.setPaymentMethod("GPay");

        if (c.getUpiId() == null) {
            System.out.println(RED + "❌ No UPI ID stored!" + RESET);
            return false;
        }

        System.out.print(CYAN + "Receiver UPI or Phone: " + RESET);
        p.setReceiverUpi(sc.nextLine());

        if (c.getGpayPin() == null) {
            System.out.println(RED + "❌ No GPay PIN!" + RESET);
            return false;
        }

        System.out.print(CYAN + "Enter amount: ₹" + RESET);
        double amt = sc.nextDouble();
        sc.nextLine();

        System.out.print(YELLOW + "Enter GPay PIN: " + RESET);
        String pin = sc.nextLine();

        if (!pin.equals(c.getGpayPin())) {
            p.setPaymentStatus("Failed");
            System.out.println(RED + "❌ Wrong PIN!" + RESET);
            return false;
        }

        if (amt >= fare) {
            p.setPaymentStatus("Success");
            System.out.println(GREEN + "✅ GPay payment successful!" + RESET);
            return true;
        } else {
            p.setPaymentStatus("Failed");
            System.out.println(RED + "❌ Insufficient amount!" + RESET);
            return false;
        }
    }

    // ✅ updated cancelBooking (makes cab available again)
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingDAO.getBookingById(bookingId);

        if (booking == null) {
            System.out.println(RED + "❌ Invalid booking id!" + RESET);
            return false;
        }

        boolean success = bookingDAO.cancelBooking(bookingId);

        if (success) {
            cabService.updateCabAvailability(booking.getCabId(), true);
            System.out.println(GREEN + "✅ Booking cancelled successfully!" + RESET);
            System.out.println(YELLOW + "🚕 Cab " + booking.getCabId() + " is now Available again." + RESET);
        }

        return success;
    }
}
