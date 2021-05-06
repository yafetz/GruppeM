package Server.Controller;

import Server.Repository.LehrmaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LehrmaterialController {


    @Autowired
    LehrmaterialRepository lehrmaterialRepository;


}
