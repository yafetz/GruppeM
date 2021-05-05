package Server.Controller;

import Server.Modell.Lehrveranstaltung;
import Server.Repository.LehrveranstaltungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lehrveranstaltung")
public class LehrveranstaltungController {

    @Autowired
    LehrveranstaltungRepository lvr;
    @GetMapping("/all")
    public List<Lehrveranstaltung> getAllLehrveranstaltungen() {
        return lvr.findAll();
    }
}
