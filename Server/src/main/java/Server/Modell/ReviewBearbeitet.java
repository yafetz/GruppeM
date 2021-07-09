package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "reviewBearbeitet")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ReviewBearbeitet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "review_id", nullable = false)
    @JsonProperty("review")
    private Review review;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "nutzer_Id", nullable = false)
    private Nutzer nutzer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }


    @Override
    public String toString() {
        return "ReviewBearbeitet{" +
                "id=" + id +
                ", review=" + review +
                ", nutzer=" + nutzer +
                '}';
    }

}
