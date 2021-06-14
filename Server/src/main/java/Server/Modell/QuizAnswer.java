package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "quizAnswer")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false)
    @JsonProperty("answer")
    private String answer;

    @Column(nullable = false)
    @JsonProperty("isCorrect")
    private boolean isCorrect;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "question_id", nullable = false)
    @JsonProperty("question")
    private QuizQuestion question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
