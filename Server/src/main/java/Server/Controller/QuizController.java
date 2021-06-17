package Server.Controller;


import Server.Modell.Quiz;
import Server.Modell.QuizAnswer;
import Server.Modell.QuizQuestion;
import Server.Repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    public QuizController(LehrveranstaltungRepository lehrveranstaltungRepository, LehrenderRepository lehrenderRepository, QuizRepository quizRepository, QuizQuestionRepository quizQuestionRepository, QuizAnswerRepository quizAnswerRepository){
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.lehrenderRepository = lehrenderRepository;
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizAnswerRepository = quizAnswerRepository;
    }

    @PostMapping("createQuiz")
    public String createQuiz(@RequestParam("questions") String questions, @RequestParam("titel") String titel ,@RequestParam("lehrenderId") long lehrenderId, @RequestParam("lehrveranstaltungsId") long lehrveranstaltungsId) throws JsonProcessingException {
        if(questions.contains("},")){
            String[] question = questions.split("},");
            Quiz quiz = new Quiz();
            quiz.setTitel(titel);
            quiz.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId));
            quiz.setLehrender(lehrenderRepository.findLehrenderById(lehrenderId));
            quizRepository.save(quiz);
            for(int i = 0; i < question.length; i++){
                String clearQuestion = question[i].replace("{","").replace("}","").replace("\"","").replace(",",":");
                String[] questionAndAnswer = clearQuestion.split(":");
                QuizQuestion qq = new QuizQuestion();
                qq.setQuiz(quiz);
                qq.setQuestion(questionAndAnswer[0]);
                quizQuestionRepository.save(qq);
                QuizAnswer qa = new QuizAnswer();
                qa.setQuestion(qq);
                int index = 0;
                for(int j = 1; j < questionAndAnswer.length; j++){
                    if(j%2 == 0){
                        //isCorrect
                        qa.setCorrect(Boolean.parseBoolean(questionAndAnswer[j]) );
                        index++;
                    }else{
                        //Antwort
                        qa.setAnswer(questionAndAnswer[j]);
                        index++;
                    }
                    if(index == 2){
                        quizAnswerRepository.save(qa);
                        qa = new QuizAnswer();
                        qa.setQuestion(qq);
                        index = 0;
                    }
                }
            }
        }
        return questions;
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
