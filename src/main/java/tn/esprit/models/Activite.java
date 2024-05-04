package tn.esprit.models;

import java.time.LocalDateTime;

public class Activite implements Comparable<Activite>{
    private int id;
    private String nom, type, description;
    private LocalDateTime date;
    public Activite(){}
    public Activite(int id, String nom, String type, String description, LocalDateTime date) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.description = description;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Activite{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
    @Override
    public int compareTo(Activite autre){ return this.id - autre.getId();}
}
