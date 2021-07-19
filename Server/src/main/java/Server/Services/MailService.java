package Server.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
private JavaMailSender mailSender;


    public void sendEmail(String email, String bewertung){
        SimpleMailMessage mailMessage= new SimpleMailMessage();
        mailMessage.setFrom("projektsep@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setText(bewertung);
        mailMessage.setSubject("Sie haben eine Bewertung!");


        mailSender.send(mailMessage);

    }

}
