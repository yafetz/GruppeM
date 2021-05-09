package Server.Controller;

import Server.Services.LehrveranstaltungBeitretenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PutMapping("{student_id}&{lehrveranstaltungsId}")
    public void beitreten(@PathVariable long lehrveranstaltungsId,@PathVariable long student_id){
        lehrveranstaltungBeitretenService.beitreten(lehrveranstaltungsId, student_id);
    }
}
