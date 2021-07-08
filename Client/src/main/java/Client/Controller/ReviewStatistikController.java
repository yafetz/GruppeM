package Client.Controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ReviewStatistikController {

    public Label statistikTitelLabel;
    public Label teilnehmerLabel;
    public Button alleBtn;
    public Button bestandenBtn;
    public Button durchgefallenBtn;

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
