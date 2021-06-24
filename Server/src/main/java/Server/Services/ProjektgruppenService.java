package Server.Services;

import Server.Modell.*;
import Server.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjektgruppenService {
    private final ProjektgruppenRepository projektgruppenRepository;
    private final GruppenmitgliedRepository gruppenmitgliedRepository;
    private final LehrenderRepository lehrenderRepository;
    private final StudentRepository studentRepository;
    private final ChatRaumRepository chatRaumRepository;

    @Autowired
    public ProjektgruppenService(ProjektgruppenRepository projektgruppenRepository, GruppenmitgliedRepository gruppenmitgliedRepository, LehrenderRepository lehrenderRepository, StudentRepository studentRepository, ChatRaumRepository chatRaumRepository) {
        this.projektgruppenRepository = projektgruppenRepository;
        this.gruppenmitgliedRepository = gruppenmitgliedRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.studentRepository = studentRepository;
        this.chatRaumRepository = chatRaumRepository;
    }

    public void createProjektgruppe (Lehrveranstaltung lehrveranstaltung, String titel, Nutzer nutzer) {
        Projektgruppe projektgruppe = new Projektgruppe(lehrveranstaltung, titel);
        ChatRaum chat = new ChatRaum();
        chatRaumRepository.save(chat);
        projektgruppe.setChat(chat);
        projektgruppenRepository.save(projektgruppe);
        Student student = studentRepository.findStudentByNutzerId(nutzer);
        if (student != null) {
            Gruppenmitglied gruppenmitglied = new Gruppenmitglied(student, projektgruppe);
            gruppenmitgliedRepository.save(gruppenmitglied);
        }
    }
}
