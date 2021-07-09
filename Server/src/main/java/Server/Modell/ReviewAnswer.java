package Server.Modell;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "reviewAnswer")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ReviewAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false)
    @JsonProperty("answer")
    private String answer;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "question_id", nullable = false)
    @JsonProperty("question")
    private ReviewQuestion question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ReviewQuestion getQuestion() {
        return question;
    }

    public void setQuestion(ReviewQuestion question) {
        this.question = question;
    }


}
