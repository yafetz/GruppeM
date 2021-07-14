package Server.Services;

import Server.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final QuizRepository quizRepository;
    private final QuizBearbeitetRepository quizBearbeitetRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewBearbeitetRepository reviewBearbeitetRepository;
    private final ReviewBearbeitetQuestionRepository reviewBearbeitetQuestionRepository;
    private final ReviewQuestionRepository reviewQuestionRepository;
    private final ReviewAnswerRepository reviewAnswerRepository;

    @Autowired
    public ReviewService(QuizRepository quizRepository, QuizBearbeitetRepository quizBearbeitetRepository, ReviewRepository reviewRepository, ReviewBearbeitetRepository reviewBearbeitetRepository, ReviewBearbeitetQuestionRepository reviewBearbeitetQuestionRepository, ReviewQuestionRepository reviewQuestionRepository, ReviewAnswerRepository reviewAnswerRepository) {
        this.quizRepository = quizRepository;
        this.quizBearbeitetRepository = quizBearbeitetRepository;
        this.reviewRepository = reviewRepository;
        this.reviewBearbeitetRepository = reviewBearbeitetRepository;
        this.reviewBearbeitetQuestionRepository = reviewBearbeitetQuestionRepository;
        this.reviewQuestionRepository = reviewQuestionRepository;
        this.reviewAnswerRepository = reviewAnswerRepository;
    }

    public int getQuizCount(long lehrveranstaltungsid){
        return quizRepository.getQuizCount(lehrveranstaltungsid);
    }

}
