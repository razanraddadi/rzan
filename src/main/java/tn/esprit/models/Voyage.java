package tn.esprit.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Voyage implements Comparable<Voyage> {
    private int id,prix;
    private String nom,destination,description,image1,type;
    private LocalDateTime date_debut,date_fin;
    public Voyage(){}
    public Voyage(int id, String nom, int prix, String destination, String description, String image1, LocalDateTime date_debut, LocalDateTime date_fin, String type) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.destination = destination;
        this.description = description;
        this.image1 = image1;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate_debut(LocalDateTime date_debut) {
        this.date_debut = date_debut;
    }

    public void setDate_fin(LocalDateTime date_fin) {
        this.date_fin = date_fin;
    }

    public LocalDateTime getDate_debut() {
        return date_debut;
    }

    public LocalDateTime getDate_fin() {
        return date_fin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Voyage{" +
                "nom=" + nom +
                ", prix='" + prix + '\'' +
                ", destination='" + destination + '\'' +
                ", date_debut='" + date_debut + '\'' +
                ", date_fin='" + date_fin + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Voyage voyage)) return false;
        return getId() == voyage.getId() && getPrix() == voyage.getPrix() && Objects.equals(getNom(), voyage.getNom()) && Objects.equals(getDestination(), voyage.getDestination()) && Objects.equals(getDescription(), voyage.getDescription()) && Objects.equals(getImage1(), voyage.getImage1()) && Objects.equals(getType(), voyage.getType()) && Objects.equals(getDate_debut(), voyage.getDate_debut()) && Objects.equals(getDate_fin(), voyage.getDate_fin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPrix(), getNom(), getDestination(), getDescription(), getImage1(), getType(), getDate_debut(), getDate_fin());
    }
    @Override
    public int compareTo(Voyage autre){ return this.id - autre.getId();}
}
