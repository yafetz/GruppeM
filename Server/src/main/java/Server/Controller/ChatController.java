package Server.Controller;

import Server.Modell.ChatNachrichten;
import Server.Modell.ChatRaum;
import Server.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/chat/")
public class ChatController {

    private final NutzerRepository nutzerRepository;
    private final StudentRepository studentRepository;
    private final LehrenderRepository lehrenderRepository;
    private final ChatNachrichtenRepository chatNachrichtenRepository;
    private final ChatRaumRepository chatRaumRepository;


    @Autowired
    public ChatController(NutzerRepository nutzerRepository, StudentRepository studentRepository, LehrenderRepository lehrenderRepository, ChatNachrichtenRepository chatNachrichtenRepository, ChatRaumRepository chatRaumRepository) {
        this.nutzerRepository = nutzerRepository;
        this.studentRepository = studentRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.chatNachrichtenRepository = chatNachrichtenRepository;
        this.chatRaumRepository = chatRaumRepository;
    }


    @PostMapping("/neueNachricht")
    public String neueNachricht(@RequestParam("chat_id") long chat_id, @RequestParam("nachricht") String nachricht,@RequestParam("datum") String datum,@RequestParam("nutzer_id") long nutzer_id) throws ParseException {
        ChatNachrichten neueNachricht = new ChatNachrichten();
        ChatRaum chat = new ChatRaum();
        chat.setId(chat_id);
        neueNachricht.setChat(chat);
        neueNachricht.setNachricht(nachricht);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(datum, formatter);
        neueNachricht.setDatum(dateTime);
        neueNachricht.setNutzer(nutzerRepository.findNutzerById(nutzer_id));
        chatNachrichtenRepository.save(neueNachricht);
        return "OK";
    }

    @GetMapping("/alleNachrichten/{chat_id}")
    public List<ChatNachrichten> alleNachrichten(@PathVariable long chat_id){
        return chatNachrichtenRepository.findChatNachrichtenByChat(chatRaumRepository.findChatRaumById(chat_id));
    }



}
