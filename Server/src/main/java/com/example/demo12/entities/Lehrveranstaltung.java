package com.example.demo12.entities;

import javax.persistence.*;

@Entity
@Table(name = "lehrveranstaltung")
public class Lehrveranstaltung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titel;
    private String typ;
    private String semester;

    public Lehrveranstaltung(){

    }

    public Lehrveranstaltung(Integer id, String titel, String typ, String semester) {
        this.id = id;
        this.titel = titel;
        this.typ = typ;
        this.semester = semester;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}

