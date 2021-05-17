package Server.Controller;
import Server.Modell.Lehrmaterial;
import Server.Modell.Lehrveranstaltung;
import Server.Repository.LehrmaterialRepository;
import Server.Repository.LehrveranstaltungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
       long id = lehrveranstaltungsId;
       Lehrveranstaltung event = lehrveranstaltungRepository.findLehrveranstaltungById(id);
       List<Lehrmaterial> materials = lehrmaterialRepository.findLehrmaterialByLehrveranstaltung(event);
       //System.out.println(materials.size()+ " ,"+ materials.get(0));
      // return "hello3";
       return materials;
   }






}
