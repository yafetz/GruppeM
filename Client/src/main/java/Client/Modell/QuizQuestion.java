package Client.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuizQuestion {

    private Long id;
    private String question;
    private Quiz quiz;

    public int getAnzahlKorrekt() {
        return anzahlKorrekt;
    }

    public void setAnzahlKorrekt(int anzahlKorrekt) {
        this.anzahlKorrekt = anzahlKorrekt;
    }

    private int anzahlKorrekt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
