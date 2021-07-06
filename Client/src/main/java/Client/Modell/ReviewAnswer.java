package Client.Modell;

public class ReviewAnswer {
    private Long id;


    private String answer;


    private boolean isCorrect;


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

    public boolean getisCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

}
