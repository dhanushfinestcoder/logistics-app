package com.example.logistics_application.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService
{


    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender)
    {
        this.mailSender=mailSender;
    }
    public String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    public void sendOtpEmail(String receiverEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiverEmail);
        message.setSubject("Order Delivery OTP");
        message.setText("Your OTP for order delivery confirmation is: " + otp);
        mailSender.send(message);
    }
}
