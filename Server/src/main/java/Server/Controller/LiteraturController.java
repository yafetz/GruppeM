package Server.Controller;

import Server.Modell.Literatur;
import Server.Modell.Thema;
import Server.Repository.LiteraturRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/literatur/")
public class LiteraturController {
    private final LiteraturRepository literaturRepository;

    public LiteraturController(LiteraturRepository literaturRepository) {
        this.literaturRepository = literaturRepository;
    }

    @GetMapping("{literatur_id}")
    public Literatur findeLiteratur(@PathVariable long literatur_id){
        return literaturRepository.findLiteraturById(literatur_id);
    }
}
