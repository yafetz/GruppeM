package Server.Controller;
import java.util.*;
import Server.Repository.LehrmaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import Server.Repository.LehrmaterialRepository;
import Server.Services.LehrmaterialService;

@RestController
@RequestMapping("/lehrmaterial/")


public class LehrmaterialController {
   @Autowired

   LehrmaterialRepository lehrmaterialRepository;

   public LehrmaterialController (LehrmaterialRepository lehrmaterialRepository) {
      this.lehrmaterialRepository = lehrmaterialRepository;
   }


   @GetMapping("/{lehrveranstaltungsId}")
  public Object alleLehrmaterialien (@PathVariable long lehrveranstaltungsId) {
       return lehrmaterialRepository.getTeachingCoursesById(lehrveranstaltungsId);
   }






}
