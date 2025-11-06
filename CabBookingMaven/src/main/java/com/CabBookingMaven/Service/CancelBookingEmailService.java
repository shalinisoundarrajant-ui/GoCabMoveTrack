package com.CabBookingMaven.Service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class CancelBookingEmailService {

    private static final String FROM_EMAIL = "";
    private static final String APP_PASSWORD = "";

    public static boolean sendCancelEmail(String toEmail, String subject, String messageText) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "GoCabMoveTrack Support Team"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            message.setText(messageText);

            Transport.send(message);

            return true;

        } catch (Exception e) {
            System.err.println("❌ Failed to send cancellation email: " + e.getMessage());
            return false;
        }
    }
}
