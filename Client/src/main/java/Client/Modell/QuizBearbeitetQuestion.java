package Client.Modell;

import Client.Modell.Nutzer;
import Client.Modell.QuizQuestion;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuizBearbeitetQuestion {
    private Long id;
    private Boolean korrekt;
    private QuizQuestion question;
    private Nutzer nutzer;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getKorrekt() {
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



    }
