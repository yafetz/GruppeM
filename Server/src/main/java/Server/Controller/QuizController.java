package Server.Controller;


import Server.Modell.*;
import Server.Repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/quiz/")

public class QuizController {

    private LehrveranstaltungRepository lehrveranstaltungRepository;
    private LehrenderRepository lehrenderRepository;
    private NutzerRepository nutzerRepository;
    private QuizRepository quizRepository;
    private QuizQuestionRepository quizQuestionRepository;
    private QuizAnswerRepository quizAnswerRepository;
    private QuizBearbeitetRepository quizBearbeitet;
    private QuizBearbeitetQuestionRepository quizBearbeitetQuestionRepository;
    private TeilnehmerListeRepository teilnehmerListeRepository;


    @Autowired
    public QuizController(NutzerRepository nutzerRepository,LehrveranstaltungRepository lehrveranstaltungRepository, LehrenderRepository lehrenderRepository, QuizRepository quizRepository, QuizQuestionRepository quizQuestionRepository, QuizAnswerRepository quizAnswerRepository, QuizBearbeitetRepository quizBearbeitet, QuizBearbeitetQuestionRepository quizBearbeitetQuestionRepository, TeilnehmerListeRepository teilnehmerListeRepository){
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizAnswerRepository = quizAnswerRepository;
        this.quizBearbeitet = quizBearbeitet;
        this.quizBearbeitetQuestionRepository = quizBearbeitetQuestionRepository;
        this.teilnehmerListeRepository=teilnehmerListeRepository;
        this.nutzerRepository = nutzerRepository;
    }

    @GetMapping("alleStudenten/{quizId}")
    public int alleStudenten(@PathVariable long quizId){
        return quizBearbeitet.getAllStudent(quizId);
    }

    @GetMapping("bestehensquote/{quizId}")
    public int bestanden(@PathVariable long quizId){
        return quizBearbeitet.getAllStudentPassed(quizId);
    }

    @GetMapping("versuche/{quizId}")
    public List<Object[]> alleStudentenVersuche(@PathVariable long quizId){
        return quizBearbeitet.getAllStudentVersuche(quizId);
    }

    @GetMapping("alleTeilnehmer/{lehrveranstaltungId}")
    public int alleTeilnehmer(@PathVariable long lehrveranstaltungId){
        return teilnehmerListeRepository.getAllStudents(lehrveranstaltungId);
    }

    @GetMapping("anzahlKorrekt/{quiz_Id}")
    public List<Object[]> alleKorrekteFragen(@PathVariable long quiz_Id){
        return quizBearbeitetQuestionRepository.getAllStudentRichtigeAntwort(quiz_Id);
    }
    @GetMapping("anzahl/{quiz_Id}")
    public List<Integer> alleKorrekteFragens(@PathVariable long quiz_Id){
        return quizBearbeitetQuestionRepository.getAllStudentRichtigeAntworten(quiz_Id);
    }


    @PostMapping("createQuiz")
    public String createQuiz(@RequestParam("titel") String titel ,@RequestParam("lehrenderId") long lehrenderId, @RequestParam("lehrveranstaltungsId") long lehrveranstaltungsId) throws JsonProcessingException {
        Quiz quiz = new Quiz();
        quiz.setTitel(titel);
        quiz.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId));
        quiz.setLehrender(lehrenderRepository.findLehrenderById(lehrenderId));
        quizRepository.save(quiz);
        return "OK: "+quiz.getId();
    }

    @PostMapping("xmlQuiz")
    public String createQuiz(@RequestParam("files") List<MultipartFile> multipartFiles, @RequestParam("lehrenderId") long lehrenderId, @RequestParam("lehrveranstaltungsId") long lehrveranstaltungsId) throws IOException {
        for (int i = 0; i < multipartFiles.size(); i++ ) {
            System.out.println(multipartFiles.get(i).getOriginalFilename());
            System.out.println("------------------------------------------");
            String titel = multipartFiles.get(i).getOriginalFilename();
            Quiz quiz = new Quiz();
            quiz.setLehrender(lehrenderRepository.findLehrenderById(lehrenderId));
            quiz.setTitel(titel);
            quiz.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId));
            quizRepository.save(quiz);
            BufferedReader br = new BufferedReader(new InputStreamReader(multipartFiles.get(i).getInputStream()));
            int countLine = 0;
            QuizQuestion qq = null;
            List<QuizAnswer> qaList = new ArrayList<>();
            while(br.ready()) {
                String line = br.readLine();
                System.out.println(line);
                if(line.contains("<Frage>")){
                    qq = new QuizQuestion();
                    qq.setQuestion(line.replace("<Frage>","").replace("</Frage>",""));
                    qq.setQuiz(quiz);
                    quizQuestionRepository.save(qq);
                }
                if(line.contains("<AntwortA>")){
                    QuizAnswer qa = new QuizAnswer();
                    qa.setQuestion(qq);
                    qa.setCorrect(false);
                    qa.setAnswer(line.replace("<AntwortA>","").replace("</AntwortA>",""));
                    qaList.add(qa);
                }
                if(line.contains("<AntwortB>")){
                    QuizAnswer qa = new QuizAnswer();
                    qa.setQuestion(qq);
                    qa.setCorrect(false);
                    qa.setAnswer(line.replace("<AntwortB>","").replace("</AntwortB>",""));
                    qaList.add(qa);
                }
                if(line.contains("<AntwortC>")){
                    QuizAnswer qa = new QuizAnswer();
                    qa.setQuestion(qq);
                    qa.setCorrect(false);
                    qa.setAnswer(line.replace("<AntwortC>","").replace("</AntwortC>",""));
                    qaList.add(qa);
                }
                if(line.contains("<AntwortD>")){
                    QuizAnswer qa = new QuizAnswer();
                    qa.setQuestion(qq);
                    qa.setCorrect(false);
                    qa.setAnswer(line.replace("<AntwortD>","").replace("</AntwortD>",""));
                    qaList.add(qa);
                }
                if(line.contains("<KorrekteAntwort>")){
                    String korrekt = line.replace("<KorrekteAntwort>","").replace("</KorrekteAntwort>","");
                    if(korrekt.contains("A")){
                        qaList.get(0).setCorrect(true);
                    }
                    if(korrekt.contains("B")){
                        qaList.get(1).setCorrect(true);
                    }
                    if(korrekt.contains("C")){
                        qaList.get(2).setCorrect(true);
                    }
                    if(korrekt.contains("D")){
                        qaList.get(3).setCorrect(true);
                    }

                    for(int a = 0; a < qaList.size(); a++){
                        quizAnswerRepository.save(qaList.get(a));
                    }
                    qaList = new ArrayList<>();
                }
                countLine++;
            }
        }
        return "OK";
    }

    @PostMapping("createQuestion")
    public String createQuizQuestionAndAnswers(@RequestParam("quiz")  long quiz,@RequestParam("question") List<String> question) throws JsonProcessingException {
        for(int j = 0; j < question.size(); j++){
            System.out.println(question.get(j));
        }
        for(int i = 0; i < question.size(); i++) {
           QuizQuestion qq = new QuizQuestion();
           qq.setQuiz(quizRepository.findById(quiz));
           qq.setQuestion(question.get(i).split(";")[0]);
           quizQuestionRepository.save(qq);
           String[] answers = question.get(i).split(";");
           for(int j = 1; j < answers.length; j+= 2){
               QuizAnswer qa = new QuizAnswer();
               qa.setQuestion(qq);
               qa.setAnswer(answers[j]);
               qa.setCorrect(Boolean.parseBoolean(answers[j+1]));
               quizAnswerRepository.save(qa);
           }
       }
        return "OK";
    }

@GetMapping("alle/{lvId}")
    public List<Quiz> getAlleQuize(@PathVariable("lvId") long lvId){
    return quizRepository.findAllByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lvId));
}

@GetMapping("alleFragen/{quizId}")
public List<QuizQuestion> getAlleFragen(@PathVariable("quizId") long quizId){
        return quizQuestionRepository.findAllByQuiz(quizRepository.findById(quizId));
}

@GetMapping("alleAntworten/{quizQuestionId}")
public List<QuizAnswer> getAlleAntworten(@PathVariable("quizQuestionId") long quizQuestionId){
        return quizAnswerRepository.findAllByQuestion(quizQuestionRepository.findById(quizQuestionId));
}

@PostMapping("bearbeitetQuiz")
public String addBearbeitetQuiz(@RequestParam("nutzerId") long nutzerId, @RequestParam("quizId") long quizId, @RequestParam("bestanden") boolean bestanden){
    QuizBearbeitet qb = new QuizBearbeitet();
    qb.setBestanden(bestanden);
    qb.setQuiz(quizRepository.findById(quizId));
    qb.setNutzer( nutzerRepository.findNutzerById(nutzerId));
        quizBearbeitet.save(qb);
return "OK";
}
    @PostMapping("bearbeitetQuizQuestion")
    public String addBearbeitetQuizQuestion(@RequestParam("nutzerId") long nutzerId, @RequestParam("questionId") long questionId, @RequestParam("korrekt") boolean korrekt){
        QuizBearbeitetQuestion qbq = new QuizBearbeitetQuestion();
        qbq.setKorrekt(korrekt);
        qbq.setQuestion(quizQuestionRepository.findById(questionId));
        qbq.setNutzer(nutzerRepository.findNutzerById(nutzerId));
        quizBearbeitetQuestionRepository.save(qbq);
        return "OK";
    }

}
