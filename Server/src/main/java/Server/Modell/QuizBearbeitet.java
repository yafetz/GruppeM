package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "quizbearbeiten")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class QuizBearbeitet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "quiz_id", nullable = false)
    @JsonProperty("quiz")
    private Quiz quiz;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "nutzer_Id", nullable = false)
    private Nutzer nutzer;

    @JsonProperty("bestanden")
    private boolean bestanden;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    public boolean getBestanden() {
        return bestanden;
    }

    public void setBestanden(boolean bestanden) {
        this.bestanden = bestanden;
    }

    @Override
    public String toString() {
        return "QuizBearbeitet{" +
                "id=" + id +
                ", quiz=" + quiz +
                ", nutzer=" + nutzer +
                ", bestanden=" + bestanden +
                '}';
    }
}
