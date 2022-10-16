package com.bank.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        if (mailSender == null) {
            // Fallback if mail is not configured
            System.out.println("Email notification (not configured): To=" + to + ", Subject=" + subject);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@bankapp.com");
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public void sendTransactionNotification(String email, String accountNumber, String operationType, String amount) {
        String subject = "Transaction Notification";
        String body = "Your account " + accountNumber + " has a " + operationType + " transaction of " + amount;
        sendEmail(email, subject, body);
    }
}
