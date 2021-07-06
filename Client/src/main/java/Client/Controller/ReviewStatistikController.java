package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Review;

public class ReviewStatistikController {
    private Layout layout;
    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Review review;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Object getNutzer() {
        return nutzer;
    }

    public void setNutzer(Object nutzer) {
        this.nutzer = nutzer;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
