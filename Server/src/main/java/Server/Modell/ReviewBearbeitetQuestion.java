package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "reviewBearbeitetQuestion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ReviewBearbeitetQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "question_id", nullable = false)
    @JsonProperty("question")
    private ReviewQuestion question;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "nutzer_Id", nullable = false)
    private Nutzer nutzer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "answer_id", nullable = false)
    private ReviewAnswer reviewAnswer;



    public ReviewQuestion getQuestion() {
        return question;
    }

    public void setQuestion(ReviewQuestion question) {
        this.question = question;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    public ReviewAnswer getReviewAnswer() {
        return reviewAnswer;
    }

    public void setReviewAnswer(ReviewAnswer reviewAnswer) {
        this.reviewAnswer = reviewAnswer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
