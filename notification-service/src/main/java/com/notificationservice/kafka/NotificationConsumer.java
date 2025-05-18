package com.notificationservice.kafka;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NotificationConsumer {

    @Autowired
    private JavaMailSender mailSender  ;

    @KafkaListener(
            topics = "${spring.kafka.topic.borrowing-notification}",
            groupId = "${spring.kafka.consumer.group-id}"
    )

    public void consumeMessage(String message) {
        System.out.println("Received message: " + message);
        String email = extractEmail(message);
        if (email != null) {
                sendEmail(email, "Borrow Request Confirmation", "Your borrow request has been created successfully.");
                System.out.println("Sent email successfully to: " + email);
        } else {
            System.out.println("Failed to extract reader's email from message.");
        }
    }
    private String extractEmail(String message) {
        try {
            String prefix = "email: ";
            int index = message.indexOf(prefix);
            if (index != -1) {
                return message.substring(index + prefix.length()).trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
            System.out.println("Email sent to: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}