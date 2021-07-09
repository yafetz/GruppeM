package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "reviewQuestion")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ReviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false)
    @JsonProperty("question")
    private String question;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "review_id", nullable = false)
    @JsonProperty("review")
    private Review review;

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
