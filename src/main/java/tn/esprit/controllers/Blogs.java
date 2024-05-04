package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import tn.esprit.models.Blog;
import tn.esprit.utils.MyDataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Blogs {


    private ObservableList<Blogs> blogs = FXCollections.observableArrayList();
    @FXML
    private Button newb;
    @FXML
    private GridPane menu_gridpane;

    @FXML
    private ScrollPane scrollpane;
    @FXML
    void onnew(ActionEvent event) {

    }
    private static Connection cnx;

    @FXML

    public void initialize(URL url, ResourceBundle rb) {
        try {
            menudisplaycard();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BlogService() {
        cnx = MyDataBase.getInstance().getConnection();
    }

    public ObservableList<Blogs> menu() throws IOException {
        String qry = "SELECT * FROM blog";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Blog blog = new Blog();
                blog.setId(rs.getInt("id"));
                blog.setTitre(rs.getString("titre"));
                blog.setDate(rs.getDate("date"));
                blog.setContent(rs.getString("content"));
                blog.setImageb(rs.getString("imageb"));
                blog.setDate(rs.getDate("date"));
               // blogs.add(blog);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return blogs;
    }

    public void menudisplaycard() throws IOException {
        blogs.clear();
        blogs.addAll(menu());
        int row = 0;
        int column = 0;
        for (int q = 0; q < blogs.size(); q++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Cardblogs.fxml"));
                AnchorPane pane = loader.load();

                // Récupération du contrôleur de la carte de voyage
                Cardblogs cardblogs = loader.getController();
               // cardblogs.SetData((Blog) blogs.get(q));
                menu_gridpane.add(pane, column++, row);
                if (column == 3) {
                    column = 0;
                    row++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}