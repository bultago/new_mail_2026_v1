package com.nugenmail.domain.mail.service;

import com.nugenmail.domain.mail.model.MailSendRequest;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * SMTP Service
 * Handles sending of emails using JavaMail SMTP.
 */
@Service
public class SmtpService {

    @Value("${spring.mail.host:localhost}")
    private String mailHost;

    @Value("${spring.mail.port:25}")
    private int mailPort;

    /**
     * Sends an email via SMTP.
     * 
     * @param username SMTP Username
     * @param password SMTP Password
     * @param request  Mail content
     * @throws MessagingException if sending fails
     */
    public void sendMessage(String username, String password, MailSendRequest request) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailHost);
        props.put("mail.smtp.port", mailPort);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username + "@" + "nugenmail.com")); // TODO: Domain handling
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(request.getTo()));
        message.setSubject(request.getSubject());
        message.setText(request.getContent());

        Transport.send(message);
    }
}
