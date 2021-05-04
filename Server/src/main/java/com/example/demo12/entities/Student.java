package com.example.demo12.entities;

import javax.persistence.*;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String studienfach;
    private Integer matrikelnummer;
    private String vornamen;
    private String nachnamen;
    private String addresse;
    private String email;
    private String passwort;
    private Integer rolle_id;

    public Student(){

    }


    public Student(Integer id, String studienfach, Integer matrikelnummer, String vornamen, String nachnamen, String addresse, String email, String passwort, Integer rolle_id) {
        this.id = id;
        this.studienfach = studienfach;
        this.matrikelnummer = matrikelnummer;
        this.vornamen = vornamen;
        this.nachnamen = nachnamen;
        this.addresse = addresse;
        this.email = email;
        this.passwort = passwort;
        this.rolle_id = rolle_id;
    }

    public String getStudienfach() {
        return studienfach;
    }

    public void setStudienfach(String studienfach) {
        this.studienfach = studienfach;
    }

    public Integer getMatrikelnummer() {
        return matrikelnummer;
    }

    public void setMatrikelnummer(Integer matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVornamen() {
        return vornamen;
    }

    public void setVornamen(String vornamen) {
        this.vornamen = vornamen;
    }

    public String getNachnamen() {
        return nachnamen;
    }

    public void setNachnamen(String nachnamen) {
        this.nachnamen = nachnamen;
    }

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public Integer getRolle_id() {
        return rolle_id;
    }

    public void setRolle_id(Integer rolle_id) {
        this.rolle_id = rolle_id;
    }
}
