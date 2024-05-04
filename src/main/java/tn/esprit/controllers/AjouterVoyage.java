package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.models.Voyage;
import tn.esprit.services.ServiceVoyage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class AjouterVoyage {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DatePicker VoyAjout_date_debut;

    @FXML
    private DatePicker VoyAjout_date_fin;

    @FXML
    private TextField TFvoy_description;

    @FXML
    private ComboBox TFvoy_destination;

    @FXML
    private TextField TFvoy_image;

    @FXML
    private TextField TFvoy_nom;

    @FXML
    private TextField TFvoy_prix;

    @FXML
    private RadioButton VoyType1;

    @FXML
    private RadioButton VoyType2;


    @FXML
    private Button confirmer_ajoutVoy_btn;

    public void populateCountryComboBox(){
        String[] countryCodes = Locale.getISOCountries();
        Arrays.sort(countryCodes);
        // Populate ComboBox with country names
        for (String countryCode : countryCodes) {
            Locale locale = new Locale("", countryCode);
            TFvoy_destination.getItems().add(locale.getDisplayCountry());
        }
    }

    @FXML
    void AjouterVoyage(ActionEvent event) {
        Random random = new Random();
        int ID = random.nextInt(Integer.MAX_VALUE) + 1;

        // Radio buttons pour le type
        String type = "";
        if(VoyType1.isSelected()){type = VoyType1.getText();}
        else if (VoyType2.isSelected()){type = VoyType2.getText();}

        String file = TFvoy_image.getText();

        // Control de saisie -DEBUT
        if (!Files.exists(Paths.get(file))) {
            afficherErreur("Le fichier spécifié n'existe pas.");
            return;
        }

        String voyageNom = TFvoy_nom.getText();
        String voyagePrixText = TFvoy_prix.getText();
        String voyageDestination = (String) TFvoy_destination.getValue();
        String voyageDescription = TFvoy_description.getText();
        String voyageImage = TFvoy_image.getText();
        LocalDate debutDate = VoyAjout_date_debut.getValue();
        LocalDate finDate = VoyAjout_date_fin.getValue();


        if (voyageNom.isEmpty()){
            TFvoy_nom.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_nom).play();
        }
        if (TFvoy_destination.getValue() == null){
            //afficherErreur("Veuillez remplir tous les champs.");
            TFvoy_destination.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_destination).play();
        }
        if(voyageDescription.isEmpty()){
            TFvoy_description.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_description).play();
        }
        if(voyagePrixText.isEmpty()){
            TFvoy_prix.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_prix).play();
        }
        if(voyageImage.isEmpty()){
            TFvoy_image.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_image).play();
        }
        if(debutDate == null){
            VoyAjout_date_debut.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(VoyAjout_date_debut).play();
        }
        if(finDate == null){
            VoyAjout_date_fin.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(VoyAjout_date_fin).play();
        }

        // TFvoy_prix est positive
        int voyagePrix;
        voyagePrix = Integer.parseInt(voyagePrixText);
        if (voyagePrix <= 0) {
            afficherErreur("Le prix du voyage doit être un nombre positif.");
            return;
        }


        LocalDate dateToday = java.time.LocalDate.now();

        // Valider la chronologie des dates
        if (!debutDate.isBefore(finDate)) {
            afficherErreur("La date de début doit être antérieure à la date de fin.");
            return;
        }
        //Valider que la date de début choisi n'est pas dans le passé
        if(debutDate.isBefore(dateToday)){
            afficherErreur("Vous pouvez pas voyager dans le passé (｡T ω T｡)");
            return;
        }
        LocalDateTime debutDateTime = debutDate.atStartOfDay();
        LocalDateTime finDateTime = finDate.atStartOfDay();
        // Control de saisie -FIN

        Voyage new_voyage = new Voyage(ID, voyageNom, voyagePrix, voyageDestination, voyageDescription,
                voyageImage, debutDateTime, finDateTime, type);

        try {
            ServiceVoyage serviceVoyage = new ServiceVoyage();
            serviceVoyage.add(new_voyage);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Le voyage a été ajouté avec succés.");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        navigateToAfficherLesVoyages(event);
    }
    @FXML
    private void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String imagePath = file.toURI().toString();
            Image image = new Image(imagePath);
            //imageviewFile.setImage(image);
            TFvoy_image.setText(file.getAbsolutePath());
        }
    }
    private void afficherErreur(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
    @FXML
    private void navigateToAfficherLesVoyages(ActionEvent event){
        try {

            FXMLLoader loader = null;
            Parent root = loader.load(getClass().getResource("/VoyageListInterface.fxml"));
            Node sourceNode = (Node) event.getSource();
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            /*
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoyageListInterface.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();*/


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        populateCountryComboBox();
    }

}