package tn.esprit.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.controllers.Blogs;
import tn.esprit.interfacesb.IService;
import tn.esprit.models.Blog;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class BlogService implements IService<Blog> {
    private ObservableList<Blogs> blogs = FXCollections.observableArrayList();

    private static Connection connection;

    public BlogService() {
        connection = MyDataBase.getInstance().getConnection();
    }
    public Blog saisie() {
        String titre, content, imageb;
        Date date;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Titre :");
        titre = scanner.nextLine();
        System.out.println("Contenu :");
        content = scanner.nextLine();
        System.out.println("Image :");
        imageb = scanner.nextLine();
        System.out.println("Date (YYYY-MM-DD) :");
        String dateString = scanner.nextLine();
        date = Date.valueOf(dateString);

        return new Blog(titre, content, imageb, date);
    }


    public ArrayList<Blog> searchByTitle(String title) {
        ArrayList<Blog> searchResults = new ArrayList<>();
        String query = "SELECT * FROM blog WHERE titre LIKE ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setString(1, "%" + title + "%");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Blog blog = new Blog();
                blog.setId(rs.getInt("id"));
                blog.setTitre(rs.getString("titre"));
                blog.setDate(rs.getDate("date"));
                blog.setContent(rs.getString("content"));
                blog.setImageb(rs.getString("imageb"));
                searchResults.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche par titre : " + e.getMessage());
        }
        return searchResults;
    }
    public ArrayList<Blog> searchByFavoris(boolean favorisValue) {
        ArrayList<Blog> searchResults = new ArrayList<>();
        String query = "SELECT * FROM blog WHERE favoris = ?";
        try {
            PreparedStatement pstm = connection.prepareStatement(query);
            pstm.setBoolean(1, favorisValue);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Blog blog = new Blog();
                blog.setId(rs.getInt("id"));
                blog.setTitre(rs.getString("titre"));
                blog.setDate(rs.getDate("date"));
                blog.setContent(rs.getString("content"));
                blog.setImageb(rs.getString("imageb"));
                blog.setFavoris(rs.getBoolean("favoris"));
                searchResults.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche par favoris : " + e.getMessage());
        }
        return searchResults;
    }


    public void add(Blog b) {
        String insert = "INSERT INTO blog (titre, content, imageb, date, favoris) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = connection.prepareStatement(insert);
            pstm.setString(1, b.getTitre());
            pstm.setString(2, b.getContent());
            pstm.setString(3, b.getImageb());
            pstm.setDate(4, new java.sql.Date(b.getDate().getTime()));
            pstm.setBoolean(5, b.isFavoris());
            pstm.executeUpdate();
            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Post ajouté avec succès !");
                pstm.executeUpdate();
            } else {
                System.out.println("Échec de l'ajout du post.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Blog> getAll() {
        ArrayList<Blog> blogs = new ArrayList<>();
        String req = "SELECT * FROM blog";
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/codeshift", "root", "");
            PreparedStatement statement = connection.prepareStatement(req);
            ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Blog blog = new Blog();
                blog.setId(rs.getInt("id"));
                blog.setTitre(rs.getString("titre"));
                blog.setDate(rs.getDate("date"));
                blog.setContent(rs.getString("content"));
                blog.setImageb(rs.getString("imageb"));
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des blogs : " + e.getMessage());
        }
        return blogs; // <- Retourne les blogs récupérés
    }

    @Override
    public void displayAll(ArrayList<Blog> items) {
        for (Blog b : items) {
            System.out.println("ID: " + b.getId());
            System.out.println("Titre: " + b.getTitre());
            System.out.println("Date: " + b.getDate());
            System.out.println("Contenu: " + b.getContent());
            System.out.println("Image: " + b.getImageb());
            System.out.println();
        }
    }
    public static class ItemNotFoundException extends Exception {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }


    public void update(Blog b) {
        try {
            if (b != null) {
                String req = "update blog  set titre =? , content =? ,imageb=? where id=?";
                PreparedStatement pstm = connection.prepareStatement(req);
                pstm.setString(1, b.getTitre());
                pstm.setString(2, b.getContent());
                pstm.setString(3, b.getImageb());
                pstm.setInt(4, b.getId());
                //pstm.executeUpdate();


                int rowsAffected = pstm.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Post modifie avec succès !");//
                } else {
                    System.out.println("Échec de modif  du post.");
                } } else {
                System.out.println("L'objet Blog passé en paramètre est null.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage()); }

    }

    @Override
    public boolean delete(Blog b) {

        String delete = "delete from blog  where id = ?";
        try {

            PreparedStatement pstm = connection.prepareStatement(delete);
            pstm.setInt(1,b.getId());
            pstm.executeUpdate();
            System.out.println("Post delete avec succès !");//

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
    public void addToFavorites(Blog blog) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {

            String query = "UPDATE blog SET favoris = ? WHERE id = ?";
            ps = connection.prepareStatement(query);
            ps.setBoolean(1, true); // true pour ajouter aux favoris
            ps.setInt(2, blog.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Blog ajouté aux favoris avec succès dans la base de données.");
            } else {
                System.out.println("L'ajout du blog aux favoris dans la base de données a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (ps != null) {
                    ps.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }}

}