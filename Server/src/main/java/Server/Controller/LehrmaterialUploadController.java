package Server.Controller;

import Server.Repository.LehrmaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LehrmaterialUploadController {

    @Autowired
    LehrmaterialRepository lehrmaterialRepository;




}
