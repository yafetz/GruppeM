package Server.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KalenderService {

    @Autowired
    private JavaMailSender mailSender;
    
    public void sendEmail(String email, String Reminder){
        SimpleMailMessage mailMessage= new SimpleMailMessage();
        mailMessage.setFrom("projektsep@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setText(Reminder);
        mailMessage.setSubject("Sie haben einen Termin!");


        mailSender.send(mailMessage);
        System.out.println("Du hast ein Termin!");
    }
}
