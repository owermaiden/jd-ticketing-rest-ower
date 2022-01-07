package com.cybertek.service;

import com.cybertek.entity.ConfirmationToken;
import com.cybertek.exeption.TicketingProjectExeption;
import org.springframework.mail.SimpleMailMessage;

public interface ConfirmationTokenService {

    ConfirmationToken save(ConfirmationToken confirmationToken);
    void sendEmail(SimpleMailMessage email);
    ConfirmationToken readByToken(String token) throws TicketingProjectExeption;
    void delete(ConfirmationToken confirmationToken);
}
