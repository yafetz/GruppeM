package Server.Controller;


import Server.Modell.Nutzer;
import Server.Repository.NutzerRepository;
import Server.Services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final NutzerRepository nutzerRepository;


    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, NutzerRepository nutzerRepository) {
        this.authenticationService = authenticationService;
        this.nutzerRepository = nutzerRepository;
    }


    @GetMapping("/{nutzerid}")
    public String sendauth(@PathVariable long nutzerid){

        Nutzer nutzer= nutzerRepository.findNutzerById(nutzerid);
        String mail = nutzer.getEmail();
        String code=String.valueOf(new Random().nextInt(9999)+1000);
        authenticationService.sendEmail(mail,code);
        nutzer.setFa_code(code);
        nutzerRepository.save(nutzer);

        return code;


    }


}
