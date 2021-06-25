package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "quizbearbeitenquestion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class QuizBearbeitetQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("korrekt")
    private boolean korrekt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "question_id", nullable = false)
    @JsonProperty("question")
    private QuizQuestion question;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "nutzer_Id", nullable = false)
    private Nutzer nutzer;

    public boolean getKorrekt() {
        return korrekt;
    }

    public void setKorrekt(Boolean korrekt) {
        this.korrekt = korrekt;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    @Override
    public String toString() {
        return "QuizBearbeitetQuestion{" +
                "id=" + id +
                ", korrekt=" + korrekt +
                ", question=" + question +
                ", nutzer=" + nutzer +
                '}';
    }
}
