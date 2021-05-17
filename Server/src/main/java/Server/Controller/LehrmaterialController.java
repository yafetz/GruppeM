package Server.Controller;
import Server.Repository.LehrmaterialRepository;
import Server.Repository.LehrveranstaltungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lehrmaterial/")


public class LehrmaterialController {
   @Autowired
   LehrveranstaltungRepository lehrveranstaltungRepository;

   LehrmaterialRepository lehrmaterialRepository;

   public LehrmaterialController (LehrmaterialRepository lehrmaterialRepository) {
      this.lehrmaterialRepository = lehrmaterialRepository;
   }


   @GetMapping("/{lehrveranstaltungsId}")
  public Object alleLehrmaterialien (@PathVariable long lehrveranstaltungsId) {
       return lehrmaterialRepository.findLehrmaterialByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId));
   }






}
