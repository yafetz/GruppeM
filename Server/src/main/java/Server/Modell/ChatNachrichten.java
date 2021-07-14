package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chatnachrichten")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ChatNachrichten {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "chat_id", nullable = false)
    private ChatRaum chat;

    @Column(nullable = false)
    @JsonProperty("nachricht")
    private String nachricht;

    @Column(nullable = false)
    @JsonProperty("datum")
    private LocalDateTime datum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "nutzer_Id", nullable = false)
    private Nutzer nutzer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNachricht() {
        return nachricht;
    }

    public void setNachricht(String nachricht) {
        this.nachricht = nachricht;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    public ChatRaum getChat() {
        return chat;
    }

    public void setChat(ChatRaum chat) {
        this.chat = chat;
    }
}
