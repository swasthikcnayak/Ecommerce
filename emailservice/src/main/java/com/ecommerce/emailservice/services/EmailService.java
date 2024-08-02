package com.ecommerce.emailservice.services;


public interface EmailService {
    
public void sendMessageWithAttachment(
    String to, String subject, String text, String pathToAttachment);
}
