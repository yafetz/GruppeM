package Server.Controller;

import Server.Modell.Student;
import Server.Services.LehrveranstaltungBeitretenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping("/beitreten/")
public class LehrveranstaltungBeitretenController {
    LehrveranstaltungBeitretenService lehrveranstaltungBeitretenService;

    @Autowired
    public LehrveranstaltungBeitretenController(LehrveranstaltungBeitretenService lehrveranstaltungBeitretenService) {
        this.lehrveranstaltungBeitretenService = lehrveranstaltungBeitretenService;
    }

    @PostMapping("{lehrveranstaltungsId}&{student_id}")
    public Object beitreten(@PathVariable long lehrveranstaltungsId,@PathVariable long student_id){
        return lehrveranstaltungBeitretenService.beitreten(lehrveranstaltungsId, student_id);
    }
}
