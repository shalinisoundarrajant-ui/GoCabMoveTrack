package com.CabBookingMaven.app;

import java.sql.Timestamp;
import java.util.Scanner;
import com.CabBookingMaven.Service.*;
import com.CabBookingMaven.dao.BookingDAO;
import com.CabBookingMaven.model.*;


public class MainApp {

    // 🎨 ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        AdminService adminService = new AdminService();
        CustomerService customerService = new CustomerService();
        CabService cabService = new CabService();
        BookingService bookingService = new BookingService();
        PaymentService paymentService = new PaymentService();
        initializeDefaultAdmin(adminService);
 
        while (true) {
            printTitle();

                System.out.println(YELLOW+"╔════════════════════════════════╗");
                System.out.println("║        CAB BOOKING MENU        ║");
                System.out.println("╠════════════════════════════════╣");
                System.out.println("║ 1. Admin Signup                ║");
                System.out.println("║ 2. Admin Login                 ║");
                System.out.println("║ 3. Customer Signup             ║");
                System.out.println("║ 4. Customer Login              ║");
                System.out.println("║ 5. Exit                        ║");
                System.out.println("╚════════════════════════════════╝");
                System.out.print("Enter choice : ");
            


            String choice = sc.nextLine();
            switch (choice) {
                case "1": handleAdminSignup(sc, adminService); break;
                case "2": handleAdminLogin(sc, adminService, cabService, bookingService, paymentService); break;
                case "3": handleCustomerSignup(sc, customerService); break;
                case "4": handleCustomerLogin(sc, customerService, cabService, bookingService, paymentService); break;
                case "5":
                    System.out.println(RED + "\n👋 Exiting application... Goodbye!" + RESET);
                    System.exit(0);
                default:
                    System.out.println(RED + "❌ Invalid choice! Please try again." + RESET);
            }
        }
    }

    private static void printTitle() {
        System.out.println(PURPLE + "===============================" + RESET);
        System.out.println(RED +
            "  🚖  " +
            PURPLE + "G" + RED + "O" + GREEN + " C" + YELLOW + "A" + CYAN + "B" + RED + "  " +
            GREEN + "M" + PURPLE + "O" + RED + "V" + GREEN + "E" + " " +
            YELLOW + "T" + CYAN + "R" + RED + "A" + PURPLE + "C" + RED + "K  " +
            "🚖" + RESET);
        System.out.println(PURPLE + "===============================" + RESET);
    }

    private static void initializeDefaultAdmin(AdminService adminService) {
        Admin admin = adminService.login("admin@cab.com", "admin123");
        if (admin == null) {
            Admin defaultAdmin = new Admin();
            defaultAdmin.setName("admin");
            defaultAdmin.setEmail("admin@cab.com");
            defaultAdmin.setPassword("admin123");
            adminService.signup(defaultAdmin);
            System.out.println(GREEN + "" + RESET);
        }
    }

    // ==================== SIGNUP & LOGIN ====================
    private static void handleAdminSignup(Scanner sc, AdminService adminService) {
        System.out.println(PURPLE + "\n--- 🧑‍💼 Admin Signup ---" + RESET);
        Admin admin = new Admin();
        System.out.print(CYAN + "Name: " + RESET);
        admin.setName(sc.nextLine());

        String email;
        while (true) {
            System.out.print(CYAN + "Email: " + RESET);
            email = sc.nextLine();
            if (adminService.isEmailExist(email)) {
                System.out.println(RED + "❌ Email already exists! Try a new email." + RESET);
            } else {
                admin.setEmail(email);
                break;
            }
        }

        if (!EmailOTPService.sendOTP(email)) {
            System.out.println(RED + "❌ Unable to send OTP. Please check your email." + RESET);
            return;
        }

        System.out.print(CYAN + "Enter OTP sent to your email: " + RESET);
        String enteredOtp = sc.nextLine();
        if (!EmailOTPService.verifyOTP(email, enteredOtp)) {
            System.out.println(RED + "❌ Invalid OTP! Signup cancelled." + RESET);
            return;
        }

        while (true) {
            System.out.print(CYAN + "Password: " + RESET);
            String pass = sc.nextLine();
            System.out.print(CYAN + "Confirm Password: " + RESET);
            String confirm = sc.nextLine();
            if (pass.equals(confirm)) {
                admin.setPassword(pass);
                break;
            }
            System.out.println(RED + "❌ Passwords do not match! Try again." + RESET);
        }

        if (adminService.signup(admin))
            System.out.println(GREEN + "✅ Admin signup successful!" + RESET);
        else
            System.out.println(RED + "❌ Signup failed!" + RESET);
    }

    private static void handleCustomerSignup(Scanner sc, CustomerService customerService) {
        System.out.println(PURPLE + "\n--- 👤 Customer Signup ---" + RESET);
        Customer customer = new Customer();

        System.out.print(CYAN + "Name: " + RESET);
        customer.setName(sc.nextLine());

        String email;
        while (true) {
            System.out.print(CYAN + "Email: " + RESET);
            email = sc.nextLine();
            if (customerService.isEmailExist(email)) {
                System.out.println(RED + "❌ Email already exists! Try a new email." + RESET);
            } else {
                customer.setEmail(email);
                break;
            }
        }

        if (!EmailOTPService.sendOTP(email)) {
            System.out.println(RED + "❌ Unable to send OTP. Please check your email." + RESET);
            return;
        }

        System.out.print(CYAN + "Enter OTP sent to your email: " + RESET);
        String enteredOtp = sc.nextLine();
        if (!EmailOTPService.verifyOTP(email, enteredOtp)) {
            System.out.println(RED + "❌ Invalid OTP! Signup cancelled." + RESET);
            return;
        }

        System.out.print(CYAN + "Phone: " + RESET);
        customer.setPhone(sc.nextLine());

        while (true) {
            System.out.print(CYAN + "Password: " + RESET);
            String pass = sc.nextLine();
            System.out.print(CYAN + "Confirm Password: " + RESET);
            String confirm = sc.nextLine();
            if (pass.equals(confirm)) {
                customer.setPassword(pass);
                break;
            }
            System.out.println(RED + "❌ Passwords do not match! Try again." + RESET);
        }

        if (customerService.signup(customer))
            System.out.println(GREEN + "✅ Customer signup successful!" + RESET);
        else
            System.out.println(RED + "❌ Signup failed!" + RESET);
    }

    private static void handleAdminLogin(Scanner sc, AdminService adminService,
                                         CabService cabService, BookingService bookingService, PaymentService paymentService) {
        System.out.println(PURPLE + "\n--- 🔐 Admin Login ---" + RESET);
        System.out.print(CYAN + "Email: " + RESET); String email = sc.nextLine();
        System.out.print(CYAN + "Password: " + RESET); String pass = sc.nextLine();

        Admin admin = adminService.login(email, pass);
        if (admin != null) {
            System.out.println(GREEN + "✅ Login successful!" + RESET);
            adminMenu(sc, cabService, bookingService, paymentService);
        } else System.out.println(RED + "❌ Invalid credentials!" + RESET);
    }

    private static void handleCustomerLogin(Scanner sc, CustomerService customerService,
                                            CabService cabService, BookingService bookingService, PaymentService paymentService) throws InterruptedException {
        System.out.println(PURPLE + "\n--- 🔐 Customer Login ---" + RESET);
        System.out.print(CYAN + "Email: " + RESET); String email = sc.nextLine();
        System.out.print(CYAN + "Password: " + RESET); String pass = sc.nextLine();

        Customer customer = customerService.login(email, pass);
        if (customer != null) {
            System.out.println(GREEN + "✅ Login successful!" + RESET);
            customerMenu(sc, customer, cabService, bookingService, paymentService);
        } else System.out.println(RED + "❌ Invalid credentials!" + RESET);
    }

    // ==================== ADMIN MENU ====================
    private static void adminMenu(Scanner sc, CabService cabService,
                                  BookingService bookingService, PaymentService paymentService) {
        boolean exit = false;
        while (!exit) {
        	 if(BookingDAO.NEW_BOOKING_ALERT > 0){
        	        System.out.println(GREEN + "🔔 ALERT: " + BookingDAO.NEW_BOOKING_ALERT + " NEW BOOKING(S)!" + RESET);
        	        BookingDAO.NEW_BOOKING_ALERT = 0;
        	    }
        	 if(BookingDAO.CANCEL_BOOKING_ALERT > 0){
        		    System.out.println(RED + "⚠️ ALERT: " + BookingDAO.CANCEL_BOOKING_ALERT + " BOOKING(S) CANCELLED!" + RESET);
        		    BookingDAO.CANCEL_BOOKING_ALERT = 0;
        		}


        	    System.out.println(PURPLE+"╔══════════════════════════════════════╗");
        	    System.out.println("║   ◤◢  CAB MANAGEMENT MENU  ◣◥       ║");
        	    System.out.println("╠══════════════════════════════════════╣");
        	    System.out.println("║ 1 ▸ Add Cab Details                  ║");
        	    System.out.println("║ 2 ▸ View Cabs Details                ║");
        	    System.out.println("║ 3 ▸ Update Cab Details               ║");
        	    System.out.println("║ 4 ▸ Delete Cab Details               ║");
        	    System.out.println("║ 5 ▸ View Bookings                    ║");
        	    System.out.println("║ 6 ▸ View Payments                    ║");
        	    System.out.println("║ 7 ▸ Logout                           ║");
        	    System.out.println("╚══════════════════════════════════════╝");
        	    System.out.print(CYAN+"Enter choice : ");
        	

            String choice = sc.nextLine();

            switch (choice) {
                case "1": addCabMenu(sc, cabService); break;
                case "2": cabService.viewAllCabs(); break;
                case "3": updateCabMenu(sc, cabService); break;
                case "4": deleteCabMenu(sc, cabService); break;
                case "5": bookingService.viewAllBookings(); break;
                case "6": paymentService.viewAllPayments(); break;
                case "7": exit = true; break;
                default: System.out.println(RED + "❌ Invalid choice!" + RESET);
            }
        }
    }

    // ==================== CUSTOMER MENU ====================
   

    private static void customerMenu(Scanner sc, Customer customer,
                                     CabService cabService, BookingService bookingService, PaymentService paymentService) throws InterruptedException {
        boolean exit = false;
        while (!exit) {
        	System.out.println("====================================");
        	System.out.println("\n");
        	System.out.println(PURPLE+"🚖 WELCOME TO GO CAB MOVE TRACK 🚖");
        	System.out.println("=====================================");
        	System.out.println(RED+"──────────────────────────────────────[2F\r\n"
        			+ "                      🚗💨💨💨\r"
        			+ "──────────────────────────────────────\r"
        			+ "");
            System.out.println(PURPLE + "\n--- 🚗 Customer Menu ---" + RESET);
           

                System.out.println(PURPLE+"╔═══════════════════════════════════════╗");
                System.out.println("║            CUSTOMER MENU              ║");
                System.out.println("╠═══════════════════════════════════════╣");
                System.out.println("║ 1 → View Available Cabs               ║");
                System.out.println("║ 2 → Book Cab                          ║");
                System.out.println("║ 3 → View My Bookings                  ║");
                System.out.println("║ 4 → Cancel Booking                    ║");
                System.out.println("║ 5 → Settings                          ║");
                System.out.println("║ 6 → Logout                            ║");
                System.out.println("╚═══════════════════════════════════════╝");
            
            System.out.print(CYAN + "Enter choice: " + RESET);
            String choice = sc.nextLine();

            switch (choice) {
                case "1": cabService.viewAvailableCabs(); break;
                case "2": bookCabFlow(sc, customer, cabService, bookingService); break;
                case "3": bookingService.viewCustomerBookings(customer.getCustomerId()); break;
                case "4": cancelBookingFlow(sc, bookingService, paymentService, customer.getCustomerId()); break;
                case "5": customerSettingsMenu(sc, customer); break;
                case "6": exit = true; break;
                default: System.out.println(RED + "❌ Invalid choice!" + RESET);
            }
        }
    }

    // ===================== CUSTOMER SETTINGS =========================
    private static String generateVerificationCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * chars.length());
            code.append(chars.charAt(index));
        }
        return code.toString();
    }

    private static void customerSettingsMenu(Scanner sc, Customer customer) {
        boolean exit = false;
        CustomerService customerService = new CustomerService();

        while (!exit) {
            System.out.println(PURPLE + "\n--- ⚙️ Settings ---" + RESET);

                System.out.println(YELLOW+"╭────────────────────────────────────╮");
                System.out.println("│        CUSTOMER SETTINGS           │");
                System.out.println("├────────────────────────────────────┤");
                System.out.println("│ 1) View Profile                    │");
                System.out.println("│ 2) Add Bank Details                │");
                System.out.println("│ 3) Set GPay PIN                    │");
                System.out.println("│ 4) Forgot GPay PIN                 │");
                System.out.println("│ 5) Back                            │");
                System.out.println("╰────────────────────────────────────╯");
            

            System.out.print(CYAN + "Enter choice: " + RESET);
            String choice = sc.nextLine();

            switch (choice) {
                // ---------------- VIEW PROFILE ----------------
                case "1":
                    System.out.println(PURPLE + "\n--- 👤 Profile Details ---" + RESET);
                    System.out.println(CYAN + "Name: " + RESET + customer.getName());
                    System.out.println(CYAN + "Email: " + RESET + customer.getEmail());
                    System.out.println(CYAN + "Phone: " + RESET + customer.getPhone());
                    System.out.println(CYAN + "GPay PIN: " + RESET +
                            (customer.getGpayPin() == null ? "❌ Not Set" : "✅ Set"));
                    System.out.println(CYAN + "Bank Name: " + RESET +
                            (customer.getBankName() == null ? "❌ Not Added" : customer.getBankName()));
                    System.out.println(CYAN + "Account Number: " + RESET +
                            (customer.getAccountNumber() == null ? "❌ Not Added" : customer.getAccountNumber()));
                    System.out.println(CYAN + "IFSC Code: " + RESET +
                            (customer.getIfscCode() == null ? "❌ Not Added" : customer.getIfscCode()));
                    System.out.println(CYAN + "UPI ID: " + RESET +
                            (customer.getUpiId() == null ? "❌ Not Generated" : customer.getUpiId()));
                    break;

                // ---------------- ADD BANK DETAILS ----------------
                case "2":
                    System.out.println(PURPLE + "\n--- 🏦 Add Bank Details ---" + RESET);

                    System.out.print(CYAN + "Enter Bank Name: " + RESET);
                    String bankName = sc.nextLine();

                    System.out.print(CYAN + "Enter Account Number: " + RESET);
                    String accNo = sc.nextLine();

                    if (!accNo.matches("\\d{9,18}")) {
                        System.out.println(RED + "❌ Invalid Account Number! Must be 9–18 digits." + RESET);
                        break;
                    }

                    System.out.print(CYAN + "Enter IFSC Code (e.g., IOBA0001234): " + RESET);
                    String ifsc = sc.nextLine().toUpperCase();

                    if (!ifsc.matches("^[A-Z]{4}0[A-Z0-9]{6}$")) {
                        System.out.println(RED + "❌ Invalid IFSC format!" + RESET);
                        break;
                    }

                    String upiSuffix = "@upi";
                    if (ifsc.startsWith("IOBA")) upiSuffix = "@iob";
                    else if (ifsc.startsWith("SBIN")) upiSuffix = "@sbi";
                    else if (ifsc.startsWith("HDFC")) upiSuffix = "@hdfcbank";
                    else if (ifsc.startsWith("ICIC")) upiSuffix = "@icici";

                    System.out.print(CYAN + "Enter preferred UPI name (e.g., rizwan): " + RESET);
                    String upiName = sc.nextLine();

                    String upiId = upiName.toLowerCase() + upiSuffix;

                    boolean bankSaved = customerService.addBankDetails(customer.getCustomerId(), bankName, accNo, ifsc, upiId);

                    if (bankSaved) {
                        customer.setBankName(bankName);
                        customer.setAccountNumber(accNo);
                        customer.setIfscCode(ifsc);
                        customer.setUpiId(upiId);

                        System.out.println(GREEN + "✅ Bank details saved successfully!" + RESET);
                        System.out.println(YELLOW + "💡 Generated UPI ID: " + CYAN + upiId + RESET);
                    } else {
                        System.out.println(RED + "❌ Failed to save bank details. Try again!" + RESET);
                    }
                    break;

                // ---------------- SET GPAY PIN ----------------
                case "3":
                    System.out.print(CYAN + "Enter new 4-digit GPay PIN: " + RESET);
                    String newPin = sc.nextLine();

                    if (!newPin.matches("\\d{4}")) {
                        System.out.println(RED + "❌ Invalid PIN format! Must be 4 digits." + RESET);
                        break;
                    }

                    System.out.print(CYAN + "Confirm new PIN: " + RESET);
                    String confirmPin = sc.nextLine();

                    if (!newPin.equals(confirmPin)) {
                        System.out.println(RED + "❌ PINs do not match! Try again." + RESET);
                        break;
                    }

                    boolean updated = customerService.updatePin(customer.getCustomerId(), newPin);
                    if (updated) {
                        customer.setGpayPin(newPin);
                        System.out.println(GREEN + "✅ GPay PIN set successfully!" + RESET);
                    } else {
                        System.out.println(RED + "❌ Failed to save PIN. Try again!" + RESET);
                    }
                    break;

                // ---------------- FORGOT GPAY PIN ----------------
                case "4":
                    System.out.print(CYAN + "Enter your registered email: " + RESET);
                    String email = sc.nextLine();

                    if (!email.equalsIgnoreCase(customer.getEmail())) {
                        System.out.println(RED + "❌ Email not found or doesn't match your account!" + RESET);
                        break;
                    }

                    String verificationCode = generateVerificationCode();
                    boolean emailSent = EmailService.sendVerificationCode(email, verificationCode);

                    if (!emailSent) {
                        System.out.println(RED + "❌ Could not send verification email. Please try again later." + RESET);
                        break;
                    }

                    System.out.print(CYAN + "Enter the verification code sent to your email: " + RESET);
                    String enteredCode = sc.nextLine();

                    if (!enteredCode.equals(verificationCode)) {
                        System.out.println(RED + "❌ Invalid verification code!" + RESET);
                        break;
                    }

                    System.out.print(CYAN + "Enter new 4-digit GPay PIN: " + RESET);
                    String resetPin = sc.nextLine();

                    if (!resetPin.matches("\\d{4}")) {
                        System.out.println(RED + "❌ Invalid PIN format! Must be 4 digits." + RESET);
                        break;
                    }

                    System.out.print(CYAN + "Re-enter new PIN: " + RESET);
                    String rePin = sc.nextLine();

                    if (!resetPin.equals(rePin)) {
                        System.out.println(RED + "❌ PINs do not match!" + RESET);
                        break;
                    }

                    boolean pinReset = customerService.updatePin(customer.getCustomerId(), resetPin);
                    if (pinReset) {
                        customer.setGpayPin(resetPin);
                        System.out.println(GREEN + "✅ GPay PIN reset successfully!" + RESET);
                    } else {
                        System.out.println(RED + "❌ Failed to reset PIN. Try again!" + RESET);
                    }
                    break;

                // ---------------- BACK ----------------
                case "5":
                    exit = true;
                    break;

                default:
                    System.out.println(RED + "❌ Invalid choice!" + RESET);
            }
        }
    }



    // ===================== CAB, BOOKING, CANCELLATION =========================
    private static void addCabMenu(Scanner sc, CabService cabService) {
        System.out.println(PURPLE + "\n--- ➕ Add Cab ---" + RESET);
        System.out.print(CYAN + "Model: " + RESET);
        String model = sc.nextLine();

        System.out.print(CYAN + "Number Plate: " + RESET);
        String number = sc.nextLine();

        System.out.print(CYAN + "Driver Name: " + RESET);
        String driver = sc.nextLine();

        System.out.print(CYAN + "Language: " + RESET);
        String language = sc.nextLine();

        System.out.print(CYAN + "Seats: " + RESET);
        int seats = Integer.parseInt(sc.nextLine());

        System.out.print(CYAN + "Fare/km: " + RESET);
        double fare = Double.parseDouble(sc.nextLine());

        System.out.print(CYAN + "Is the cab available now? (yes/no): " + RESET);
        String availabilityInput = sc.nextLine().trim().toLowerCase();
        boolean isAvailable = availabilityInput.equals("yes");

        cabService.addCab(model, number, driver, language, fare, seats, isAvailable);

        System.out.println(GREEN + "✅ Cab added successfully!" + RESET);
    }
    private static void updateCabMenu(Scanner sc, CabService cabService) {
        System.out.println(PURPLE + "\n--- ✏️ Update Cab Details ---" + RESET);
        System.out.print(CYAN + "Enter Cab ID: " + RESET);
        int cabId = Integer.parseInt(sc.nextLine());
        Cab cab = cabService.getCabById(cabId);
        if (cab == null) {
            System.out.println(RED + "❌ Cab not found!" + RESET);
            return;
        }

        System.out.println(YELLOW + "\nCurrent Cab Details:" + RESET);
        System.out.println(CYAN + "Model: " + cab.getModel());
        System.out.println("Driver: " + cab.getDriverName());
        System.out.println("Language: " + cab.getDriverLanguage());
        System.out.println("Seats: " + cab.getSeatCapacity());
        System.out.println("Availability: " + (cab.isAvailability() ? "Available" : "Under Maintenance"));
        System.out.println("Fare/km: " + cab.getFarePerKm() + RESET);

        boolean exit = false;
        while (!exit) {
            System.out.println(CYAN+"╔══════════════════════════════╗");
            System.out.println("║     ◤◢  EDIT CAB DATA  ◣◥   ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║ 1 ▸ Model                    ║");
            System.out.println("║ 2 ▸ Driver                   ║");
            System.out.println("║ 3 ▸ Language                 ║");
            System.out.println("║ 4 ▸ Seats                    ║");
            System.out.println("║ 5 ▸ Availability             ║");
            System.out.println("║ 6 ▸ Fare                     ║");
            System.out.println("║ 7 ▸ Exit                     ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print(CYAN + "Select field to update: " + RESET);
            String ch = sc.nextLine();
            switch (ch) {
                case "1":
                    System.out.print(CYAN + "Enter new Model: " + RESET);
                    cabService.updateCabField(cabId, "model", sc.nextLine());
                    break;
                case "2":
                    System.out.print(CYAN + "Enter new Driver Name: " + RESET);
                    cabService.updateCabField(cabId, "driver_name", sc.nextLine());
                    break;
                case "3":
                    System.out.print(CYAN + "Enter new Driver Language: " + RESET);
                    cabService.updateCabField(cabId, "driver_language", sc.nextLine());
                    break;
                case "4":
                    System.out.print(CYAN + "Enter new Seat Capacity: " + RESET);
                    cabService.updateCabField(cabId, "seat_capacity", Integer.parseInt(sc.nextLine()));
                    break;
                case "5":
                    System.out.print(CYAN + "Available? (yes/no): " + RESET);
                    cabService.updateCabAvailability(cabId, sc.nextLine().equalsIgnoreCase("yes"));
                    break;
                case "6":
                    System.out.print(CYAN + "Enter new Fare/km: " + RESET);
                    cabService.updateCabField(cabId, "fare_per_km", Double.parseDouble(sc.nextLine()));
                    break;
                case "7": exit = true; break;
                default: System.out.println(RED + "❌ Invalid choice!" + RESET);
            }
        }
    }

    private static void deleteCabMenu(Scanner sc, CabService cabService) {
        System.out.println(PURPLE + "\n--- 🗑️ Delete Cab ---" + RESET);
        System.out.print(CYAN + "Enter Cab ID: " + RESET);
        int cabId = Integer.parseInt(sc.nextLine());

        System.out.print(YELLOW + "⚠️ Are you sure you want to delete this cab? (yes/no): " + RESET);
        String confirm = sc.nextLine();
        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println(YELLOW + "🚫 Deletion cancelled." + RESET);
            return;
        }

        if (cabService.deleteCab(cabId))
            System.out.println(GREEN + "✅ Cab deleted successfully!" + RESET);
        else
            System.out.println(RED + "❌ Failed to delete cab!" + RESET);
    }
    private static void bookCabFlow(Scanner sc, Customer customer, CabService cabService, BookingService bookingService) {

        // ------------------- STEP 0: Show Available Cabs -------------------
        cabService.viewAvailableCabs();

        int cabId;
        Cab selectedCab = null;

        // ------------------- STEP 1: Validate Cab ID -------------------
        while (true) {
            System.out.print(CabService.CYAN + "Enter Cab ID: " + CabService.RESET);
            try {
                cabId = Integer.parseInt(sc.nextLine().trim());
                selectedCab = cabService.getCabById(cabId);

                if (selectedCab == null) {
                    System.out.println(CabService.RED + "❌ Cab not found. Please enter a valid Cab ID." + CabService.RESET);
                } else if (!selectedCab.isAvailability()) {
                    System.out.println(CabService.RED + "❌ Cab is not available (Booked or Under Maintenance)." + CabService.RESET);
                } else {
                    System.out.println(CabService.GREEN + "✅ Cab found and available for booking!" + CabService.RESET);
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println(CabService.RED + "⚠️ Please enter a numeric Cab ID." + CabService.RESET);
            }
        }

        // ------------------- STEP 2: Collect Booking Details -------------------
        System.out.print(CabService.CYAN + "Pickup: " + CabService.RESET);
        String pickup = sc.nextLine();

        System.out.print(CabService.CYAN + "Drop: " + CabService.RESET);
        String drop = sc.nextLine();

        System.out.print(CabService.CYAN + "Passengers: " + CabService.RESET);
        int passengers = Integer.parseInt(sc.nextLine());

        System.out.print(CabService.CYAN + "Enter Estimated Distance (km): " + CabService.RESET);
        double distance = Double.parseDouble(sc.nextLine());

        // ------------------- STEP 3: OTP Verification -------------------
        if (!EmailOTPService.sendOTP(customer.getEmail())) {
            System.out.println(CabService.RED + "❌ Unable to send OTP. Please check your email." + CabService.RESET);
            return;
        }

        System.out.print(CabService.CYAN + "Enter OTP sent to your email: " + CabService.RESET);
        String enteredOtp = sc.nextLine();

        if (!EmailOTPService.verifyOTP(customer.getEmail(), enteredOtp)) {
            System.out.println(CabService.RED + "❌ Invalid OTP! Booking cancelled." + CabService.RESET);
            return;
        }

        // ------------------- STEP 4: Booking -------------------
        boolean isBooked = bookingService.bookCab(customer, selectedCab.getCabId(), pickup, drop, passengers, distance);

        if (isBooked) {
            System.out.println(CabService.GREEN + "✅ Cab booked successfully!" + CabService.RESET);

            // Optionally mark cab unavailable
            cabService.updateCabAvailability(selectedCab.getCabId(), false);

            // ------------------- STEP 5: Send Booking Confirmation Email -------------------
            String subject = "🚖 Cab Booking Confirmation - Cab ID: " + selectedCab.getCabId();
            String message = """
                    Dear %s,

                    ✅ Your cab has been successfully booked!

                    📍 Pickup Location: %s
                    🎯 Drop Location: %s
                    🚘 Cab ID: %d
                    👥 Passengers: %d
                    📏 Estimated Distance: %.2f km

                    Thank you for booking with GoCabMoveTrack! 🚖

                    Regards,
                    GoCabMoveTrack Booking Team
                    """.formatted(
                    customer.getName(),
                    pickup,
                    drop,
                    selectedCab.getCabId(),
                    passengers,
                    distance
            );

            boolean emailSent = BookingEmailService.sendBookingConfirmation(customer.getEmail(), subject, message);

            if (emailSent) {
                System.out.println(CabService.YELLOW + "📩 Booking confirmation email sent to " + customer.getEmail() + CabService.RESET);
            } else {
                System.out.println(CabService.RED + "⚠️ Booking confirmed, but failed to send email notification." + CabService.RESET);
            }

        } else {
            System.out.println(CabService.RED + "❌ Booking failed! Please try again." + CabService.RESET);
        }
    }


    private static void cancelBookingFlow(Scanner sc, BookingService bookingService, PaymentService paymentService, int customerId) throws InterruptedException {

        bookingService.viewCustomerBookings(customerId);

        System.out.print(CYAN + "Enter Booking ID to cancel: " + RESET);
        int bookingId = Integer.parseInt(sc.nextLine());

        System.out.print(YELLOW + "Press 'C' to confirm: " + RESET);
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("C")) {

            String code = generateVerificationCode(5);

            System.out.println(PURPLE + "\nType this code to confirm: " + RESET + code);
            System.out.print(CYAN + "Enter code: " + RESET);
            String input = sc.nextLine();

            if (input.equals(code)) {

                // ================= CHECK BOOKING TIME ======================
                Booking booking = bookingService.getBookingById(bookingId);
                Timestamp bookingTime = booking.getBookingTime();
                Timestamp now = new Timestamp(System.currentTimeMillis());

                long diffSeconds = (now.getTime() - bookingTime.getTime()) / 1000;
                long diffMinutes = diffSeconds / 60;

                double refundAmount = booking.getAmount();

                if (diffMinutes >= 2) {
                    System.out.println(YELLOW + "⏳ Cancellation after 2 minutes!" + RESET);
                    System.out.println(YELLOW + "10% deduction applied." + RESET);
                    refundAmount = refundAmount * 0.90;
                } else {
                    System.out.println(GREEN + "✅ Cancellation within 2 minutes → Full Refund" + RESET);
                }

                // ================== CANCEL BOOKING =========================
                boolean cancelled = bookingService.cancelBooking(bookingId);

                if(cancelled) {
                    System.out.println(GREEN + "✅ Booking cancelled!" + RESET);

                    // ================== PROCESS REFUND ======================
                    paymentService.processRefund(bookingId, refundAmount);
                    System.out.println(GREEN + "💰 Refund Amount Credited: ₹" + refundAmount + RESET);

                    //================ SEND CANCEL EMAIL ==================
                    Customer customer = CustomerService.getCustomerById(customerId);
                    String email = customer.getEmail();

                    String subject = "🚫 Cab Booking Cancelled - Booking ID: " + bookingId;
                    String msg = """
                            Dear %s,

                            Your cab booking has been cancelled successfully ❌

                            🔹 Booking ID: %d
                            🔹 Refund Amount: ₹%.2f
                            🔹 Date: %s

                            Thank you for choosing GoCabMoveTrack 💛

                            Regards,
                            GoCabMoveTrack Team
                            """.formatted(customer.getName(), bookingId, refundAmount, java.time.LocalDate.now().toString());

                    CancelBookingEmailService.sendCancelEmail(email, subject, msg);

                    System.out.println(YELLOW + "📩 Cancellation Email sent to " + email + RESET);

                } else {
                    System.out.println(RED + "❌ Booking cancel failed!" + RESET);
                }

            } else {
                System.out.println(RED + "❌ Code mismatch!" + RESET);
            }
        }
    }

    private static String generateVerificationCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < length; i++) code.append(chars.charAt(r.nextInt(chars.length())));
        return code.toString();
    }
}
