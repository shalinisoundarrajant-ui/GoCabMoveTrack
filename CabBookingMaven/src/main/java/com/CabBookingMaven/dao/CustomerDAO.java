package com.CabBookingMaven.dao;

import com.CabBookingMaven.model.Customer;
import java.sql.*;

public class CustomerDAO {

    // signup
    public boolean signup(Customer c) {
        String sql = "INSERT INTO customer(name,email,password,phone,pin,bank_name,account_number,ifsc_code,upi_id) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPassword());
            ps.setString(4, c.getPhone());
            ps.setString(5, c.getGpayPin());
            ps.setString(6, c.getBankName());
            ps.setString(7, c.getAccountNumber());
            ps.setString(8, c.getIfscCode());
            ps.setString(9, c.getUpiId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) c.setCustomerId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) { System.out.println(e); }
        return false;
    }

    // login
    public Customer login(String email, String password) {
        String sql = "SELECT * FROM customer WHERE email=? AND password=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapCustomer(rs);

        } catch (SQLException e) { System.out.println(e); }
        return null;
    }

    // check by email
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE email=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapCustomer(rs);

        } catch (SQLException e) { System.out.println(e); }
        return null;
    }

    // NEW → get by id
    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM customer WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapCustomer(rs);

        } catch (SQLException e) { System.out.println(e); }
        return null;
    }

    // update bank
    public boolean addBankDetails(int customerId, String bank, String acc, String ifsc, String upi) {
        String sql = "UPDATE customer SET bank_name=?,account_number=?,ifsc_code=?,upi_id=? WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bank);
            ps.setString(2, acc);
            ps.setString(3, ifsc);
            ps.setString(4, upi);
            ps.setInt(5, customerId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) { System.out.println(e); }
        return false;
    }

    // update PIN
    public boolean updatePin(int customerId, String newPin) {
        String sql = "UPDATE customer SET pin=? WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPin);
            ps.setInt(2, customerId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) { System.out.println(e); }
        return false;
    }

    // mapper
    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getInt("customer_id"));
        c.setName(rs.getString("name"));
        c.setEmail(rs.getString("email"));
        c.setPassword(rs.getString("password"));
        c.setPhone(rs.getString("phone"));
        c.setGpayPin(rs.getString("pin"));
        c.setBankName(rs.getString("bank_name"));
        c.setAccountNumber(rs.getString("account_number"));
        c.setIfscCode(rs.getString("ifsc_code"));
        c.setUpiId(rs.getString("upi_id"));
        return c;
    }
}
