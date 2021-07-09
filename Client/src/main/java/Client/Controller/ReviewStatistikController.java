package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Review;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ReviewStatistikController {
    public Label statistikTitelLabel;
    public Label teilnehmerLabel;
    public Button alleBtn;
    public Button bestandenBtn;
    public Button durchgefallenBtn;
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

    public void allePressedButton(ActionEvent actionEvent) {
        teilnehmerLabel.setText("Alle Teilnehmer");

    }

    public void bestandenPressedButton(ActionEvent actionEvent) {
        teilnehmerLabel.setText("Nur Teilnehmer, die bestanden haben");

    }

    public void durchgefallenPressedButton(ActionEvent actionEvent) {
        teilnehmerLabel.setText("Nur Teilnehmer, die nicht bestanden haben");

    }
}
