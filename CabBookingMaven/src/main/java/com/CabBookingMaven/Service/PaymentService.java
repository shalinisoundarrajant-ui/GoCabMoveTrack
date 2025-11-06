package com.CabBookingMaven.Service;

import com.CabBookingMaven.dao.PaymentDAO;
import com.CabBookingMaven.model.Payment;
import java.util.List;

public class PaymentService {
    private PaymentDAO dao = new PaymentDAO();

    // 🔹 Make a new payment
    public boolean makePayment(Payment payment) {
        boolean result = dao.savePayment(payment);
        if (result)
            System.out.println("");
        else
            System.out.println("❌ Payment failed to record.");
        return result;
    }

    // 🔹 View all payments (Admin)
    public void viewAllPayments() {
        List<Payment> list = dao.getAllPayments();
        if (list.isEmpty()) {
            System.out.println("⚠️ No payment records found.");
        } else {
            System.out.println("\n--- 💳 Payment Transactions ---");
            for (Payment p : list) {
                System.out.println(p);
            }
        }
    }

    public void processRefund(int bookingId, double refundAmount) {
        Payment p = new Payment();
        p.setBookingId(bookingId);
        p.setAmount(refundAmount);
        p.setPaymentMethod("Refund");
        p.setPaymentStatus("Success");
        dao.savePayment(p);
    }

	
}
