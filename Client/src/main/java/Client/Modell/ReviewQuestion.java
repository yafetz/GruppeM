package Client.Modell;

public class ReviewQuestion {
    private Long id;
    private String question;
    private Review review;

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

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

}
