package com.e_commerce.service;

import com.e_commerce.entity.ContactMessage;
import com.e_commerce.repository.ContactMessageRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ContactMessageRepo contactMessageRepo;

    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendUserDetails(String name, String userEmail, String subject, String message) throws MessagingException {

        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setMessage(message);
        contactMessage.setUserEmail(userEmail);
        contactMessage.setSubject(subject);
        contactMessage.setName(name);
        contactMessageRepo.save(contactMessage);

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Set the recipient (your email address)
        helper.setTo("useme1315@gmail.com");

        // Set the subject
        helper.setSubject(subject);

        // Create the HTML content with bold labels
        String htmlContent = "<html>" +
                "<body>" +
                "<p><strong>Name:</strong> " + name + "</p>" +
                "<p><strong>User Email:</strong> " + userEmail + "</p>" +
                "<p><strong>Message:</strong> <br>" + message + "</p>" +
                "</body>" +
                "</html>";

        // Set the email content as HTML
        helper.setText(htmlContent, true);

        // Send the email
        emailSender.send(mimeMessage);
    }

}
