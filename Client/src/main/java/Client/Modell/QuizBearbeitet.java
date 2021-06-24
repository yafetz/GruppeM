package Client.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuizBearbeitet {

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

    public Boolean getBestanden() {
        return bestanden;
    }

    public void setBestanden(Boolean bestanden) {
        this.bestanden = bestanden;
    }

    private Long id;
    private Quiz quiz;
    private Nutzer nutzer;
    private Boolean bestanden;
}
