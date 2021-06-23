package Client.Modell;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Termin {
    private Long id;
    private String titel;
    private String von;
    private String bis;
    private int reminderValue;
    private String reminderArt;
    private String reminderShow;
    private Lehrveranstaltung lehrveranstaltung;
    private Nutzer nutzer_Id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public LocalDateTime getVon() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(von.replace("T"," "), formatter);
    }

    public void setVon(String von) {
        this.von = von;
    }

    public LocalDateTime getBis() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(bis.replace("T"," "), formatter);
    }

    public void setBis(String bis) {
        this.bis = bis;
    }

    public int getReminderValue() {
        return reminderValue;
    }

    public void setReminderValue(int reminderValue) {
        this.reminderValue = reminderValue;
    }

    public String getReminderArt() {
        return reminderArt;
    }

    public void setReminderArt(String reminderArt) {
        this.reminderArt = reminderArt;
    }

    public String getReminderShow() {
        return reminderShow;
    }

    public void setReminderShow(String reminderShow) {
        this.reminderShow = reminderShow;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Nutzer getNutzer_Id() {
        return nutzer_Id;
    }

    public void setNutzerId(Nutzer nutzer_Id) {
        this.nutzer_Id = nutzer_Id;
    }
}
