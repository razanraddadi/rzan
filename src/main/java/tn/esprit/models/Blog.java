package tn.esprit.models;

import tn.esprit.controllers.BlogList;

import java.util.Date;

public class Blog extends BlogList {
    private int id;
    private String titre;
    private String content;
    private String imageb;

    private Date date;
    private boolean favoris=false;
    public Blog() {
    }
    //public Blog( int id,String titre, String content, String imageb) {

         public Blog(int id, String titre, String content, String imageb, Date date, boolean favoris) {
        this.id = id;
        this.titre = titre;
        this.content = content;
        this.imageb = imageb;
        this.date = date;
        this.favoris = favoris;
    }

    public Blog(String titre, String content, String imageb, java.sql.Date date) {

    }

    public static void setImageP(String i) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageb() {
        return imageb;
    }

    public void setImageb(String imageb) {
        this.imageb = imageb;
    }
    public boolean isFavoris() {
        return favoris;
    }

    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
    }
    public Date getDate() { return date;}

    public void setDate(Date date) {
        this.date = date; }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", content='" + content + '\'' +
                ", imageb='" + imageb + '\'' +
                ", date=" + date +
                "}\n";
    }

 //   public ObservableValue<String> titreProperty() {return null;}

  //  public ObservableValue<String> contentProperty() {return null;}

   // public ObservableValue<String> imagebProperty() {return null;}

    // public ObservableValue<String> dateProperty() {return null;}
}
