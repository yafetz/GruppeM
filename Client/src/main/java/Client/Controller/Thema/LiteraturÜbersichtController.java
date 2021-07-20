package Client.Controller.Thema;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Literatur;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LiteraturÜbersichtController {
    @FXML
    public Label art;
    @FXML
    public Label author;
    @FXML
    public Label title;
    @FXML
    public Label year;
    @FXML
    public Label pub;
    @FXML
    public Label pages;
    @FXML
    public Label volume;
    @FXML
    public Label issn;
    @FXML
    public Label journal;
    @FXML
    public Label edition;
    @FXML
    public Label editor;
    @FXML
    public Label price;
    @FXML
    public Label isbn;
    @FXML
    public Label doi;
    @FXML
    public Label file;
    @FXML
    public Label institution;
    @FXML
    public Label keywords;
    @FXML
    public Label series;
    @FXML
    public Label url;
    @FXML
    public Label booktitle;
    @FXML
    public Label abstrakt;
    @FXML
    public Label address;
    @FXML
    public Label number;
    @FXML
    public Label urldate;


    @FXML
    public Label Jahr;
    @FXML
    public Label Urldate;
    @FXML
    public Label Author;
    @FXML
    public Label Nummer;
    @FXML
    public Label Herausgeber;
    @FXML
    public Label Seiten;
    @FXML
    public Label Volume;
    @FXML
    public Label ISSN;
    @FXML
    public Label ISBN;
    @FXML
    public Label Journal;
    @FXML
    public Label Edition;
    @FXML
    public Label Editor;
    @FXML
    public Label Preis;
    @FXML
    public Label File;
    @FXML
    public Label Institution;
    @FXML
    public Label Keywords;
    @FXML
    public Label Serie;
    @FXML
    public Label DOI;
    @FXML
    public Label URL;
    @FXML
    public Label Booktitle;
    @FXML
    public Label Abstrakt;
    @FXML
    public Label Address;


    private Literatur literatur;
    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Literatur getLiteratur() {
        return literatur;
    }

    public void setLiteratur(Literatur literatur) {
        this.literatur = literatur;
        literaturAufrufen();
    }

    public void literaturAufrufen() {
        if (literatur.getAdress() != null) {
            address.setText(literatur.getAdress());

        } else {
            address.setVisible(false);
            Address.setVisible(false);
        }
        if (literatur.getAbstract() != null) {
            abstrakt.setText(literatur.getAbstract());

        } else {
            abstrakt.setVisible(false);
            Abstrakt.setVisible(false);
        }
        if (literatur.getAuthor() != null) {
            author.setText(literatur.getAuthor());
        } else {
            author.setVisible(false);
            Author.setVisible(false);
        }
        if (literatur.getBooktitle() != null) {
            booktitle.setText(literatur.getBooktitle());
        } else {
            booktitle.setVisible(false);
            Booktitle.setVisible(false);
        }
        if (literatur.getDoi() != null) {
            doi.setText(literatur.getDoi());
        } else {
            doi.setVisible(false);
            DOI.setVisible(false);
        }
        if (literatur.getEdition() != null) {
            edition.setText(literatur.getEdition());
        } else {
            edition.setVisible(false);
            Edition.setVisible(false);
        }
        if (literatur.getEditor() != null) {
             editor.setText(literatur.getEditor());
        } else {
            editor.setVisible(false);
            Editor.setVisible(false);
        }
        if (literatur.getFile() != null) {
          file.setText(literatur.getFile());
        } else {
            file.setVisible(false);
            File.setVisible(false);
        }
        if (literatur.getInstitution() != null) {
            institution.setText(literatur.getInstitution());
        } else {
            institution.setVisible(false);
            Institution.setVisible(false);
        }
        if (literatur.getIsbn() != null) {
            isbn.setText(literatur.getIsbn());
        } else {
            isbn.setVisible(false);
            ISBN.setVisible(false);
        }
        if (literatur.getIssn() != null) {
            issn.setText(literatur.getIssn());
        } else {
            issn.setVisible(false);
            ISSN.setVisible(false);
        }
        if (literatur.getJournal() != null) {
            journal.setText(literatur.getJournal());
        } else {
            journal.setVisible(false);
            Journal.setVisible(false);
        }
        if (literatur.getKeywords() != null) {
            keywords.setText(literatur.getKeywords());
        } else {
            keywords.setVisible(false);
            Keywords.setVisible(false);
        }
        if (literatur.getNumber() != null) {
            number.setText(literatur.getNumber());
        } else {
            number.setVisible(false);
            Nummer.setVisible(false);
        }
        if (literatur.getPages() != null) {
            pages.setText(literatur.getPages());
        } else {
            pages.setVisible(false);
            Seiten.setVisible(false);
        }
        if (literatur.getPrice() != null) {
            price.setText(literatur.getPrice());
        } else {
            price.setVisible(false);
            Preis.setVisible(false);

        }
        if (literatur.getPublisher() != null) {
            pub.setText(literatur.getPublisher());
        } else {
            pub.setVisible(false);
            Herausgeber.setVisible(false);
        }
        if (literatur.getSeries() != null) {
            series.setText(literatur.getSeries());
        } else {
            series.setVisible(false);
            Serie.setVisible(false);
        }
        if (literatur.getTitle() != null) {
            title.setText(literatur.getTitle());

        } else {
            title.setVisible(false);
        }
        if (literatur.getType() != null) {
            art.setText(literatur.getType());

        } else {
            art.setVisible(false);
        }
        if (literatur.getUrl() != null) {
            url.setText(literatur.getUrl());

        } else {
            url.setVisible(false);
            URL.setVisible(false);
        }
        if (literatur.getUrldate() != null) {
            urldate.setText(literatur.getUrldate());
        } else {
            urldate.setVisible(false);
            Urldate.setVisible(false);
        }
        if (literatur.getVolume() != null) {
            volume.setText(literatur.getVolume());
        } else {
            volume.setVisible(false);
            Volume.setVisible(false);
        }
        if (literatur.getYear() != null) {
            year.setText(literatur.getYear());

        } else {
            year.setVisible(false);
            Jahr.setVisible(false);
        }


    }
}
