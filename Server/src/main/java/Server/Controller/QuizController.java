package Server.Controller;


import Server.Modell.*;
import Server.Repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz/")

public class QuizController {

    private LehrveranstaltungRepository lehrveranstaltungRepository;
    private LehrenderRepository lehrenderRepository;
    private QuizRepository quizRepository;
    private QuizQuestionRepository quizQuestionRepository;
    private QuizAnswerRepository quizAnswerRepository;
    private QuizBearbeitetRepository quizBearbeitet;
    private QuizBearbeitetQuestionRepository quizBearbeitetQuestionRepository;
    private TeilnehmerListeRepository teilnehmerListeRepository;

    @Autowired
    public QuizController(LehrveranstaltungRepository lehrveranstaltungRepository, LehrenderRepository lehrenderRepository, QuizRepository quizRepository, QuizQuestionRepository quizQuestionRepository, QuizAnswerRepository quizAnswerRepository, QuizBearbeitetRepository quizBearbeitet, QuizBearbeitetQuestionRepository quizBearbeitetQuestionRepository, TeilnehmerListeRepository teilnehmerListeRepository){
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizAnswerRepository = quizAnswerRepository;
        this.quizBearbeitet = quizBearbeitet;
        this.quizBearbeitetQuestionRepository = quizBearbeitetQuestionRepository;
        this.teilnehmerListeRepository=teilnehmerListeRepository;
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
}
