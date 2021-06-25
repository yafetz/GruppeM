package Server.Modell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "freundschaft")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Freundschaft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "anfragender_id", nullable = false)
    private Nutzer anfragender_nutzer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="angefragter_id", nullable=false)
    private Nutzer angefragter_nutzer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "chat_id", nullable = true)
    //@JsonProperty("chatRaum")
    private ChatRaum chat;

    @JoinColumn(name = "status", nullable = true )
    private boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Nutzer getAnfragender_nutzer() {
        return anfragender_nutzer;
    }

    public void setAnfragender_nutzer(Nutzer anfragender_nutzer) {
        this.anfragender_nutzer = anfragender_nutzer;
    }

    public Nutzer getAngefragter_nutzer() {
        return angefragter_nutzer;
    }

    public void setAngefragter_nutzer(Nutzer angefragter_nutzer) {
        this.angefragter_nutzer = angefragter_nutzer;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ChatRaum getChat() {
        return chat;
    }

    public void setChat(ChatRaum chat) {
        this.chat = chat;
    }
}
