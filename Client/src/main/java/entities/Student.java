package entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Student {
    private Integer id;
    private String studienfach;
    private Integer matrikelnummer;
    private String vornamen;
    private String nachnamen;
    private String addresse;
    private String email;
    private String passwort;
    private Integer rolle_id;

    @JsonCreator

    public Student(@JsonProperty("id") int id,
                   @JsonProperty("studienfach") String studienfach,
                   @JsonProperty("vornamen") String  vornamen,
                   @JsonProperty("nachnamen") String nachnamen,
                   @JsonProperty("email") String email,
                   @JsonProperty("passwort") String passwort,
                   @JsonProperty("matrikelnummer") int matrikelnummer,
                   @JsonProperty("rolle_id") int rolle_id
    ){
        this.id=id;
        this.studienfach=studienfach;
        this.vornamen=vornamen;
        this.nachnamen=nachnamen;
        this.email=email;
        this.passwort=passwort;
        this.matrikelnummer=matrikelnummer;
        this.rolle_id=rolle_id;
    }
}
