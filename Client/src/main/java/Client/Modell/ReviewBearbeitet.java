package Client.Modell;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewBearbeitet {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Review getReview() {
        return review;
    }

    public void setQuiz(Review quiz) {
        this.review = review;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }



    private Long id;
    private Review review;
    private Nutzer nutzer;

}
