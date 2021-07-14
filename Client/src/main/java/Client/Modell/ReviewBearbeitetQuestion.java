package Client.Modell;

public class ReviewBearbeitetQuestion {

    private Long id;
    private Boolean korrekt;
    private ReviewQuestion question;
    private Nutzer nutzer;
    private ReviewAnswer reviewAnswer;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
