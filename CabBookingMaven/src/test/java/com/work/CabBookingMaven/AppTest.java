package com.work.CabBookingMaven;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {

    private double ratePerKm;
    private double distance;
    private int cabSeats;
    private int passengers;
    private String originalOTP;
    private String enteredOTP;

    @Before
    public void setUp() {
        // This runs before each test
        ratePerKm = 12.0;
        distance = 10.0;
        cabSeats = 4;
        passengers = 3;
        originalOTP = "1234";
        enteredOTP = "1234";
    }

    @Test
    public void testFareCalculation() {
        double expected = 120.0;
        double actual = ratePerKm * distance;
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testSeatCapacity() {
        boolean result = (passengers <= cabSeats);
        assertTrue(result);
    }

    @Test
    public void testOtpMatch() {
        assertEquals(originalOTP, enteredOTP);
    }
}
