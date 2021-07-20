package Server.Controller;

import Server.Modell.*;
import Server.Repository.*;
import Server.Services.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TeilnehmerListeRepository teilnehmerListeRepository;
    

    @Autowired
    public ReviewController(ReviewService reviewService, QuizBearbeitetRepository quizBearbeitetRepository, QuizRepository quizRepository, LehrenderRepository lehrenderRepository, LehrveranstaltungRepository lehrveranstaltungRepository, ReviewRepository reviewRepository, ReviewQuestionRepository reviewQuestionRepository, ReviewAnswerRepository reviewAnswerRepository, NutzerRepository nutzerRepository, ReviewBearbeitetRepository reviewBearbeitetRepository, ReviewBearbeitetQuestionRepository reviewBearbeitetQuestionRepository, TeilnehmerListeRepository teilnehmerListeRepository) {
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
        this.teilnehmerListeRepository = teilnehmerListeRepository;
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
        List<ReviewBearbeitet> reviewBearbeitet;
        if (reviewRepository.findByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsid)) != null && reviewBearbeitetRepository.findByNutzer(nutzerRepository.findNutzerById(nutzerid)) != null) {
            review = reviewRepository.findByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsid));

            reviewBearbeitet = reviewBearbeitetRepository.findByNutzer(nutzerRepository.findNutzerById(nutzerid));
            for(int i=0; i<reviewBearbeitet.size();i++) {
                if (review.getId() == reviewBearbeitet.get(i).getReview().getId()) {
                    return true;
                    
                }
            }

        } else {
            return false;
        }

        return false;
    }

    @GetMapping("/threshold/{nutzerid}&{lehrveranstaltungsid}")
    public boolean checkThreshold(@PathVariable("nutzerid") long nutzerid, @PathVariable("lehrveranstaltungsid") long lehrveranstaltungsid){
        float anzahl = quizRepository.getQuizCount(lehrveranstaltungsid);

        float bearbeitet = quizBearbeitetRepository.getNutzerquiz(nutzerid, lehrveranstaltungsid);

    if(bearbeitet>0.0 && anzahl>0.0){
        if(bearbeitet/anzahl >=0.5){
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
    public String addBearbeitetReviewQuestion(@RequestParam("nutzerId") long nutzerId, @RequestParam("questionId") long questionId, @RequestParam("answer") List<Long> answer){

        long id =0;
//        System.out.println("Answer List "+ answer.toString());
        for(int i=0; i<answer.size();i++) {
            ReviewBearbeitetQuestion rbq = new ReviewBearbeitetQuestion();
            rbq.setQuestion(reviewQuestionRepository.findById(questionId));
            rbq.setNutzer(nutzerRepository.findNutzerById(nutzerId));
            id=answer.get(i);
            rbq.setReviewAnswer(reviewAnswerRepository.findById(id));
            reviewBearbeitetQuestionRepository.save(rbq);
//            System.out.println("Answer gespeichert " + id);
        }
        return "OK";
    }

    @GetMapping("{lehrveranstaltungsid}")
    public Review getReview(@PathVariable("lehrveranstaltungsid") long lehrveranstaltungsid){
        return reviewRepository.findByLehrveranstaltung(lehrveranstaltungRepository.findLehrveranstaltungById(lehrveranstaltungsid));
    }



    @GetMapping("hatLvBestanden/{nutzerId}&{lehrveranstaltungsId}")
    public boolean hatLehrveranstaltungBestanden(@PathVariable("nutzerId") long nutzerId, @PathVariable("lehrveranstaltungsId") long lehrveranstaltungsId) {
        float anzahlInsgesamt = quizRepository.getQuizCount(lehrveranstaltungsId);
        float anzahlBestanden = quizBearbeitetRepository.getAnzahlQuizBestanden(nutzerId, lehrveranstaltungsId);

        if ( (anzahlBestanden > 0.0) && (anzahlInsgesamt > 0.0) ) {
            if( (anzahlBestanden/anzahlInsgesamt) >= 0.5){
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @GetMapping("teilnehmerAlle/frageId={frageId}")
    public Integer getAnzahlAntwortenAlleTeilnehmerFuerFrage(@PathVariable("frageId") long frageId) {
        Lehrveranstaltung lv = reviewQuestionRepository.findById(frageId).getReview().getLehrveranstaltung();
        List<Student> students = teilnehmerListeRepository.getAllStudByLehrveranstaltungId(lv.getId());

        int count = 0;
        for(Student student : students) {
            count += reviewBearbeitetQuestionRepository.getAntwortenCountByQuestionIdAndNutzerId(frageId, student.getNutzerId().getId());
        }
        return count;
    }

    @GetMapping("teilnehmerBestanden/frageId={frageId}")
    public Integer getAnzahlAntwortenBestandenTeilnehmerFuerFrage(@PathVariable("frageId") long frageId) {
        Lehrveranstaltung lv = reviewQuestionRepository.findById(frageId).getReview().getLehrveranstaltung();
        List<Student> students = teilnehmerListeRepository.getAllStudByLehrveranstaltungId(lv.getId());

        int count = 0;
        for(Student student : students) {
            if(hatLehrveranstaltungBestanden(student.getNutzerId().getId(), lv.getId())) {
                count += reviewBearbeitetQuestionRepository.getAntwortenCountByQuestionIdAndNutzerId(frageId, student.getNutzerId().getId());
            }
        }
        return count;
    }

    @GetMapping("teilnehmerNichtBestanden/frageId={frageId}")
    public Integer getAnzahlAntwortenNichtBestandenTeilnehmerFuerFrage(@PathVariable("frageId") long frageId) {
        return (getAnzahlAntwortenAlleTeilnehmerFuerFrage(frageId) - getAnzahlAntwortenBestandenTeilnehmerFuerFrage(frageId));
    }


    @GetMapping("teilnehmerAlle/antwortId={antwortId}")
    public Integer getAnzahlAuswahlenFuerAntwortAlleTeilnehmer(@PathVariable("antwortId") long antwortId) {
        Lehrveranstaltung lv = reviewAnswerRepository.findById(antwortId).getQuestion().getReview().getLehrveranstaltung();
        List<Student> students = teilnehmerListeRepository.getAllStudByLehrveranstaltungId(lv.getId());

        int count = 0;
        for(Student student : students) {
           count += reviewBearbeitetQuestionRepository.getEineAntwortCountByAnswerIdAndNutzerId(antwortId, student.getNutzerId().getId());
        }
        return count;
    }

    @GetMapping("teilnehmerBestanden/antwortId={antwortId}")
    public Integer getAnzahlAuswahlenFuerAntwortBestandenTeilnehmer(@PathVariable("antwortId") long antwortId) {
        Lehrveranstaltung lv = reviewAnswerRepository.findById(antwortId).getQuestion().getReview().getLehrveranstaltung();
        List<Student> students = teilnehmerListeRepository.getAllStudByLehrveranstaltungId(lv.getId());

        int count = 0;
        for(Student student : students) {
            if(hatLehrveranstaltungBestanden(student.getNutzerId().getId(), lv.getId())) {
                count += reviewBearbeitetQuestionRepository.getEineAntwortCountByAnswerIdAndNutzerId(antwortId, student.getNutzerId().getId());
            }
        }
        return count;
    }
    @GetMapping("teilnehmerNichtBestanden/antwortId={antwortId}")
    public Integer getAnzahlAuswahlenFuerAntwortNichtBestandenTeilnehmer(@PathVariable("antwortId") long antwortId) {
        return (getAnzahlAuswahlenFuerAntwortAlleTeilnehmer(antwortId) - getAnzahlAuswahlenFuerAntwortBestandenTeilnehmer(antwortId));
    }
}
