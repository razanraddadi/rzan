package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox NavigEvent;
    @FXML
    private VBox NavigVoyage;
    @FXML
    private VBox navblog;

    @FXML
    void ajoutevent(MouseEvent event) {
        System.out.println("I NAVIGATED");

        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/AjouterEvent.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Node sourceNode = (Node) event.getSource();
        Stage stage;
        stage = (Stage) sourceNode.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


}
    @FXML
    private void navigateToAjouterVoyage(MouseEvent event) {
        // Load the FXML file of the next page
        System.out.println("I NAVIGATED");

        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/AjouterVoyage.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Node sourceNode = (Node) event.getSource();
        Stage stage;
        stage = (Stage) sourceNode.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void navigateToAjouterBlog(MouseEvent event) {
        System.out.println("I NAVIGATED");

        FXMLLoader loader;
        Parent root;
        try {
            loader = new FXMLLoader(getClass().getResource("/AjouterBlog.fxml"));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Node sourceNode = (Node) event.getSource();
        Stage stage;
        stage = (Stage) sourceNode.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void initialize() {


    }
}
