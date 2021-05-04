package com.example.demo12.entities;

import javax.persistence.*;

@Entity
@Table(name = "lehrende")
public class Lehrender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lehrstuhl;
    private String forschungsgebiet;

    private String vornamen;
    private String nachnamen;
    private String addresse;
    private String email;
    private String passwort;
    private Integer rolle_id;

    public Lehrender(Integer id, String lehrstuhl, String forschungsgebiet, String vornamen, String nachnamen, String addresse, String email, String passwort, Integer rolle_id) {
        this.id = id;
        this.lehrstuhl = lehrstuhl;
        this.forschungsgebiet = forschungsgebiet;
        this.vornamen = vornamen;
        this.nachnamen = nachnamen;
        this.addresse = addresse;
        this.email = email;
        this.passwort = passwort;
        this.rolle_id = rolle_id;
    }

    public Lehrender(){

    }




    public String getLehrstuhl() {
        return lehrstuhl;
    }

    public void setLehrstuhl(String lehrstuhl) {
        this.lehrstuhl = lehrstuhl;
    }

    public String getForschungsgebiet() {
        return forschungsgebiet;
    }

    public void setForschungsgebiet(String forschungsgebiet) {
        this.forschungsgebiet = forschungsgebiet;
    }
}
