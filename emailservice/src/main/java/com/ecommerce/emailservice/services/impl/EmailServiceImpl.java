package com.ecommerce.emailservice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecommerce.emailservice.services.EmailService;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(
      String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("noreply@baeldung.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }


    @Override
public void sendMessageWithAttachment(
  String to, String subject, String text, String pathToAttachment) {
    // ...
    
    // MimeMessage message = emailSender.createMimeMessage();
     
    // MimeMessageHelper helper = new MimeMessageHelper(message, true);
    
    // helper.setFrom("noreply@baeldung.com");
    // helper.setTo(to);
    // helper.setSubject(subject);
    // helper.setText(text);
        
    return ;
    // FileSystemResource file 
    //   = new FileSystemResource(new File(pathToAttachment));
    // helper.addAttachment("Invoice", file);

    // emailSender.send(message);
    // ...
}
}