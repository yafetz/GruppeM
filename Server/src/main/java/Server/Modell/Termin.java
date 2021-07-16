package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "termin")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titel;

    @Column(nullable = false)
    private LocalDateTime von;

    @Column(nullable = false)
    private LocalDateTime bis;

    @Column(nullable = false)
    private int reminderValue;

    @Column(nullable = false)
    private String reminderArt;

    @Column(nullable = false)
    private String reminderShow;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "lehrveranstaltungs_Id", nullable = false)
    @JsonProperty("lehrveranstaltung")
    private Lehrveranstaltung lehrveranstaltung;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="nutzer_Id", nullable=true)
    @JsonProperty("nutzer_Id")
    private Nutzer nutzerId;

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
        return von;
    }

    public void setVon(LocalDateTime von) {
        this.von = von;
    }

    public LocalDateTime getBis() {
        return bis;
    }

    public void setBis(LocalDateTime bis) {
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

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Nutzer getNutzerId() {
        return nutzerId;
    }

    public void setNutzerId(Nutzer nutzerId) {
        this.nutzerId = nutzerId;
    }

    public String getReminderShow() {
        return reminderShow;
    }

    public void setReminderShow(String reminderShow) {
        this.reminderShow = reminderShow;
    }
    
}
