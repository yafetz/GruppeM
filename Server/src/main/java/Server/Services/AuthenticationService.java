package Server.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// projektsep@gmail.com
// Passwort: sep123456789

@Service
public class AuthenticationService {

  @Autowired
  private JavaMailSender mailSender;
    public void sendEmail(String email, String code){
        SimpleMailMessage mailMessage= new SimpleMailMessage();
        mailMessage.setFrom("projektsep@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setText(code);
        mailMessage.setSubject("Emailcode");


        mailSender.send(mailMessage);
        System.out.println("Mail gesendet          "+mailMessage);


    }
}
