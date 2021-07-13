package Server.Controller;

import Server.Modell.*;
import Server.Repository.*;
import Server.Services.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review/")
public class ReviewController {

    private ReviewService reviewService;
    private LehrenderRepository lehrenderRepository;
    private LehrveranstaltungRepository lehrveranstaltungRepository;
    private ReviewRepository reviewRepository;
    private ReviewQuestionRepository reviewQuestionRepository;
    private ReviewAnswerRepository reviewAnswerRepository;
    private NutzerRepository nutzerRepository;
    private ReviewBearbeitetQuestionRepository reviewBearbeitetQuestionRepository;
    private ReviewBearbeitetRepository reviewBearbeitetRepository;
    private QuizRepository quizRepository;
    private QuizBearbeitetRepository quizBearbeitetRepository;
    

    @Autowired
    public ReviewController(ReviewService reviewService,QuizBearbeitetRepository quizBearbeitetRepository ,QuizRepository quizRepository ,LehrenderRepository lehrenderRepository, LehrveranstaltungRepository lehrveranstaltungRepository, ReviewRepository reviewRepository, ReviewQuestionRepository reviewQuestionRepository, ReviewAnswerRepository reviewAnswerRepository, NutzerRepository nutzerRepository, ReviewBearbeitetRepository reviewBearbeitetRepository, ReviewBearbeitetQuestionRepository reviewBearbeitetQuestionRepository) {
        this.reviewService = reviewService;
        this.lehrenderRepository = lehrenderRepository;
        this.lehrveranstaltungRepository = lehrveranstaltungRepository;
        this.reviewRepository = reviewRepository;
        this.reviewQuestionRepository = reviewQuestionRepository;
        this.reviewBearbeitetQuestionRepository = reviewBearbeitetQuestionRepository;
        this.reviewAnswerRepository = reviewAnswerRepository;
        this.nutzerRepository = nutzerRepository;
        this.reviewBearbeitetRepository = reviewBearbeitetRepository;
        this.quizRepository = quizRepository;
        this.quizBearbeitetRepository = quizBearbeitetRepository;
    }

    @GetMapping("count/{lehrveranstaltungsid}")
    public int getQuizCount(@PathVariable long lehrveranstaltungsid) {
        return reviewService.getQuizCount(lehrveranstaltungsid);
    }

    @PostMapping("createReview")
    public String createReview(@RequestParam("titel") String titel, @RequestParam("lehrenderId") long lehrenderId, @RequestParam("lehrveranstaltungsId") long lehrveranstaltungsId) {
        Review review = new Review();
        review.setTitel(titel);
        review.setLehrender(lehrenderRepository.findLehrenderById(lehrenderId));
        review.setLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsId));
        reviewRepository.save(review);
        return "OK: " + review.getId();
    }

    @PostMapping("createQuestion")
    public String createReviewQuestionAndAnswers(@RequestParam("review") long review, @RequestParam("question") List<String> question) throws JsonProcessingException {
//        for (int j = 0; j < question.size(); j++) {
//            System.out.println(question.get(j));
//        }
        for (int i = 0; i < question.size(); i++) {
            ReviewQuestion rq = new ReviewQuestion();
            rq.setReview(reviewRepository.findById(review));
            rq.setQuestion(question.get(i).split(";")[0]);
            reviewQuestionRepository.save(rq);
            String[] answers = question.get(i).split(";");
            for (int j = 1; j < answers.length; j++) {
                ReviewAnswer reviewAnswer = new ReviewAnswer();
                reviewAnswer.setQuestion(rq);
                reviewAnswer.setAnswer(answers[j]);
                reviewAnswerRepository.save(reviewAnswer);
            }
        }
        return "OK";
    }

    @PostMapping("bearbeitetReview")
    public String addBearbeitetReview(@RequestParam("nutzerId") long nutzerId, @RequestParam("reviewId") long reviewId) {
        ReviewBearbeitet rb = new ReviewBearbeitet();
        rb.setReview(reviewRepository.findById(reviewId));
        rb.setNutzer(nutzerRepository.findNutzerById(nutzerId));
        reviewBearbeitetRepository.save(rb);
        return "OK";
    }

    @GetMapping("alleFragen/{lvId}")
    public List<ReviewQuestion> getAlleFragen(@PathVariable("lvId") long lehrveranstaltungId) {
        Lehrveranstaltung lv = lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungId);
        return reviewQuestionRepository.findAllByReview(reviewRepository.findByLehrveranstaltung(lv));
    }

    @GetMapping("check/{lehrveranstaltungsid}")
    public boolean checkIfExists(@PathVariable("lehrveranstaltungsid") long lehrveranstaltungsid) {

        if (reviewRepository.findByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsid)) != null) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("check/user/{nutzerid}&{lehrveranstaltungsid}")
    public boolean checkIfReviewed(@PathVariable("nutzerid") long nutzerid, @PathVariable("lehrveranstaltungsid") long lehrveranstaltungsid) {
        Review review;
        ReviewBearbeitet reviewBearbeitet;
        if (reviewRepository.findByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsid)) != null && reviewBearbeitetRepository.findByNutzer(nutzerRepository.findNutzerById(nutzerid)) != null) {
            review = reviewRepository.findByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsid));
            reviewBearbeitet = reviewBearbeitetRepository.findByNutzer(nutzerRepository.findNutzerById(nutzerid));
            if (review.getId() == reviewBearbeitet.getReview().getId()) {
                return true;
            }

        } else {
            return false;
        }

        return false;
    }

    @GetMapping("/threshold/{nutzerid}&{lehrveranstaltungsid}")
    public boolean checkThreshold(@PathVariable("nutzerid") long nutzerid, @PathVariable("lehrveranstaltungsid") long lehrveranstaltungsid){
        float anzahl = quizRepository.getQuizCount(lehrveranstaltungsid);

        float bestanden = quizBearbeitetRepository.getNutzerquiz(nutzerid, lehrveranstaltungsid);

    if(bestanden>0.0 && anzahl>0.0){
        if(anzahl/bestanden >=0.5){
            return true;
        }
    }
    else{
        return false;
    }
        return false;
    }



    @GetMapping("alleAntworten/{reviewQuestionId}")
    public List<ReviewAnswer> getAlleAntworten(@PathVariable("reviewQuestionId") long reviewQuestionId){
        return reviewAnswerRepository.findAllByQuestion(reviewQuestionRepository.findById(reviewQuestionId));
    }

    @PostMapping("bearbeitetReviewQuestion")
    public String addBearbeitetReviewQuestion(@RequestParam("nutzerId") long nutzerId, @RequestParam("questionId") long questionId, @RequestParam("answer") String answer){
        ReviewBearbeitetQuestion rbq = new ReviewBearbeitetQuestion();
        rbq.setQuestion(reviewQuestionRepository.findById(questionId));
        rbq.setNutzer(nutzerRepository.findNutzerById(nutzerId));
        rbq.setReviewAnswer(answer);
        reviewBearbeitetQuestionRepository.save(rbq);
        return "OK";
    }

    @GetMapping("{lehrveranstaltungsid}")
    public Review getReview(@PathVariable("lehrveranstaltungsid") long lehrveranstaltungsid){
        return reviewRepository.findByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsid));
    }

    @GetMapping("teilnehmerAlle/frageId={frageId}")
    public Integer getAnzahlAllerTeilnehmerAnFrage(@PathVariable("frageId") long frageId) {
        List<Nutzer> nutzerliste = reviewBearbeitetQuestionRepository.findAllByQuestionId(frageId);
        int count = 0;
        for(Nutzer nutzer : nutzerliste) {
            count += 1;
        }
        return count;
    }
    @GetMapping("teilnehmerBestanden/frageId={frageId}")
    public Integer getAnzahlTeilnehmerBestandenAnFrage(@PathVariable("frageId") long frageId) {
        ReviewQuestion frage = reviewQuestionRepository.findById(frageId);
        List<Nutzer> nutzerliste = reviewBearbeitetQuestionRepository.findAllByQuestionId(frageId);
        int count = 0;
        for(Nutzer nutzer : nutzerliste) {
            if(checkThreshold(nutzer.getId(), frage.getReview().getLehrveranstaltung().getId())) {
                count += 1;
            }
        }
        return count;
    }
    @GetMapping("teilnehmerNichtBestanden/frageId={frageId}")
    public Integer getAnzahlTeilnehmerNichtBestandenAnFrage(@PathVariable("frageId") long frageId) {
        return (getAnzahlAllerTeilnehmerAnFrage(frageId) - getAnzahlTeilnehmerBestandenAnFrage(frageId));
    }


    @GetMapping("teilnehmerAlle/antwortId={antwortId}")
    public Integer getAnzahlAllerTeilnehmerAntwort(@PathVariable("antwortId") long antwortId) {
        return 0;
    }
    @GetMapping("teilnehmerBestanden/antwortId={antwortId}")
    public Integer getAnzahlTeilnehmerBestandenAntwort(@PathVariable("antwortId") long antwortId) {
        return 0;
    }
    @GetMapping("teilnehmerNichtBestanden/antwortId={antwortId}")
    public Integer getAnzahlTeilnehmerNichtBestandenAntwort(@PathVariable("antwortId") long antwortId) {
        return 0;
    }
}
