package tn.esprit.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.models.Voyage;
import tn.esprit.services.ServiceVoyage;

public class ModifierVoyage {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Blog_btn;

    @FXML
    private Button Event_btn;

    @FXML
    private Button Home_btn;

    @FXML
    private Button ListVoyage_btn;

    @FXML
    private Button Spot_btn;

    @FXML
    private TextField TFvoy_description_old;

    @FXML
    private ComboBox TFvoy_destination_old;

    @FXML
    private TextField TFvoy_image_old;

    @FXML
    private TextField TFvoy_nom_old;

    @FXML
    private TextField TFvoy_prix_old;

    @FXML
    private HBox VosVoyages_btn;

    @FXML
    private DatePicker VoyAjout_date_debut_old;

    @FXML
    private DatePicker VoyAjout_date_fin_old;

    @FXML
    private RadioButton VoyType1_old;

    @FXML
    private RadioButton VoyType2_old;

    @FXML
    private Button modifier_btn;

    @FXML
    private ToggleGroup type_voyage;

    ServiceVoyage sv = new ServiceVoyage();
    ArrayList<Voyage> list_voyages = sv.getAll();

    private String nameToSearchWith ="";
    void getNameToSearchWith(Voyage voyage){
        nameToSearchWith = voyage.getNom();
        System.out.println("the word is :" + nameToSearchWith);
    }
    public void populateCountryComboBox(){
        String[] countryCodes = Locale.getISOCountries();
        Arrays.sort(countryCodes);
        // Populate ComboBox with country names
        for (String countryCode : countryCodes) {
            Locale locale = new Locale("", countryCode);
            TFvoy_destination_old.getItems().add(locale.getDisplayCountry());
        }
    }
    void setData(Voyage voyage){
        TFvoy_description_old.setText(voyage.getDescription());
        TFvoy_destination_old.setValue(voyage.getDestination());
        TFvoy_image_old.setText(voyage.getImage1());
        TFvoy_nom_old.setText(voyage.getNom());
        TFvoy_prix_old.setText(String.valueOf(voyage.getPrix()));
        VoyAjout_date_debut_old.setValue(voyage.getDate_debut().toLocalDate());
        VoyAjout_date_fin_old.setValue(voyage.getDate_fin().toLocalDate());
        if(voyage.getType().equals("Touristique")) {
            VoyType1_old.setSelected(true);
        }else if(voyage.getType().equals("Humanitaire") ){
            VoyType2_old.setSelected(true);
        }
        System.out.println("WE ARE PUTTING STUFF HERE");
    }
    @FXML
    void EditVoyage(ActionEvent event) {
        int ID = 0;
        Voyage chosenvoyage = null;
        for (Voyage unit : list_voyages) {
            System.out.println("the unit is named : " + unit.getNom());
            if (unit.getNom().toLowerCase().equals(nameToSearchWith.toLowerCase())) {
                chosenvoyage = unit;
                ID = unit.getId();
                break; // Break out of the loop after finding the voyage
            }
        }
        if (chosenvoyage == null) {
            System.out.println("it's NULLLLLL");
        }
        String type = "";
        if (VoyType1_old.isSelected()) {
            type = VoyType1_old.getText();
        } else if (VoyType2_old.isSelected()) {
            type = VoyType2_old.getText();
        }

        String file = TFvoy_image_old.getText();

        // Control de saisie -DEBUT
        if (!Files.exists(Paths.get(file))) {
            afficherErreur("Le fichier spécifié n'existe pas.");
            return;
        }

        String voyageNom = TFvoy_nom_old.getText();
        String voyagePrixText = TFvoy_prix_old.getText();
        String voyageDestination = (String) TFvoy_destination_old.getValue();
        String voyageDescription = TFvoy_description_old.getText();
        String voyageImage = TFvoy_image_old.getText();
        LocalDate debutDate = VoyAjout_date_debut_old.getValue();
        LocalDate finDate = VoyAjout_date_fin_old.getValue();


        if (voyageNom.isEmpty()){
            TFvoy_nom_old.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_nom_old).play();
            return;
        }
        if (TFvoy_destination_old.getValue() == null){
            TFvoy_destination_old.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_destination_old).play();
            return;
        }
        if(voyageDescription.isEmpty()){
            TFvoy_description_old.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_description_old).play();
            return;
        }
        if(voyagePrixText.isEmpty()){
            TFvoy_prix_old.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_prix_old).play();
            return;
        }
        if(voyageImage.isEmpty()){
            TFvoy_image_old.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(TFvoy_image_old).play();
            return;
        }
        if(debutDate == null){
            VoyAjout_date_debut_old.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(VoyAjout_date_debut_old).play();
            return;
        }
        if(finDate == null){
            VoyAjout_date_fin_old.setStyle("-fx-border-color :red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(VoyAjout_date_fin_old).play();
            return;
        }

        // TFvoy_prix est positive
        int voyagePrix;
        voyagePrix = Integer.parseInt(voyagePrixText);
        if (voyagePrix <= 0) {
            afficherErreur("Le prix du voyage doit être un nombre positif.");
            return;
        }

        //LocalDate dateToday = java.time.LocalDate.now();

        // Valider la chronologie des dates
        /*
        if (!debutDate.isBefore(finDate)) {
            afficherErreur("La date de début doit être antérieure à la date de fin.");
            return;
        }
        //Valider que la date de début choisi n'est pas dans le passé
        if(debutDate.isBefore(dateToday)){
            afficherErreur("Vous pouvez pas voyager dans le passé (｡T ω T｡)");
            return;
        }*/
        LocalDateTime debutDateTime = debutDate.atStartOfDay();
        LocalDateTime finDateTime = finDate.atStartOfDay();
        // Control de saisie -FIN

        Voyage update = new Voyage(ID, voyageNom, voyagePrix, voyageDestination, voyageDescription,
                voyageImage, debutDateTime, finDateTime, type);
        try {
            ServiceVoyage serviceVoyage = new ServiceVoyage();
            serviceVoyage.update(update);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Le voyage a été modifié avec succés.");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Retour à la liste
        navigateToAfficherLesVoyages(event);

    }

    private void afficherErreur(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void choisirImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String imagePath = file.toURI().toString();
            Image image = new Image(imagePath);
            //imageviewFile.setImage(image);
            TFvoy_image_old.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void navigateToAfficherLesVoyages(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoyageListInterface.fxml"));
            Parent root = loader.load();
            Node sourceNode = (Node) event.getSource();
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        populateCountryComboBox();
    }

}