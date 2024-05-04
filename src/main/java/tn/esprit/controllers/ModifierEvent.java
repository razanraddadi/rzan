package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import tn.esprit.models.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.models.Event;
import tn.esprit.services.EventService;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;

public class ModifierEvent {
    @FXML
    private TextField eventNameTextField;

    @FXML
    private TextArea eventDescriptionTextArea;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField URLImage;

    @FXML
    private TextField capacityTextField;

    @FXML
    private TextField destinationTextField;

    @FXML
    private TextField eventPrixTextField;

    @FXML
    private TextField reservedTextField;

    @FXML
    private ImageView eventImageView;

    private Event selectedEvent;
    private String nameToSearchWith ="";

    public void initData(Event event) {
        selectedEvent = event;
        eventNameTextField.setText(event.getName());
        eventDescriptionTextArea.setText(event.getDescription());


        // Chargement et affichage de l'image
        selectedEvent.setImageFile(URLImage.getText());

        capacityTextField.setText(String.valueOf(event.getCapacity()));
        destinationTextField.setText(event.getDestination());
        eventPrixTextField.setText(String.valueOf(event.getPrix()));
        reservedTextField.setText(String.valueOf(event.getReserved()));
    }


    @FXML
    void chargerImage(ActionEvent event) throws FileNotFoundException, IOException {
        Random rand = new Random();
        int x = rand.nextInt(1000);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        String DBPath = "" + x + ".jpg";

        if (file != null) {
            FileInputStream source = new FileInputStream(file.getAbsolutePath());
            FileOutputStream destination = new FileOutputStream(DBPath);
            BufferedInputStream bin = new BufferedInputStream(source);
            BufferedOutputStream bou = new BufferedOutputStream(destination);

            String path = file.getAbsolutePath();
            String res = path.substring(47, path.length()); // Adjust this based on your file path
            System.out.println(res);


            Image img = new Image(file.toURI().toString());
            eventImageView.setImage(img);

            URLImage.setText(res); //Set image URL for the event

            int b;
            while ((b = bin.read()) != -1) {
                bou.write(b);
            }

            bin.close();
            bou.close();

        } else {
            System.out.println("Error: No file selected");
        }
    }

    @FXML
    private void modifierEvent(ActionEvent event) throws SQLException {
        try {
            // Mettre à jour les détails de l'événement avec les valeurs des champs de l'interface utilisateur
            selectedEvent.setName(eventNameTextField.getText());
            selectedEvent.setDescription(eventDescriptionTextArea.getText());
            selectedEvent.setDate(String.valueOf(java.sql.Date.valueOf(startDatePicker.getValue())));
            selectedEvent.setEnd(String.valueOf(java.sql.Date.valueOf(endDatePicker.getValue())));
            selectedEvent.setImageFile(URLImage.getText());
            selectedEvent.setCapacity(Integer.parseInt(capacityTextField.getText()));
            selectedEvent.setDestination(destinationTextField.getText());
            selectedEvent.setPrix(Double.parseDouble(eventPrixTextField.getText()));
            selectedEvent.setReserved(Integer.parseInt(reservedTextField.getText()));

            // Appeler le service pour modifier l'événement
            EventService eventService = new EventService();
            eventService.modifier(selectedEvent);

        Stage stage = (Stage) eventNameTextField.getScene().getWindow();
        stage.close();
        } catch (SQLException ex) {
            // Gérer les exceptions de base de données
            ex.printStackTrace();
            // Afficher un message d'erreur à l'utilisateur si nécessaire
            // ...
        } catch (NumberFormatException ex) {
            // Gérer les exceptions de conversion de type pour les champs numériques
            ex.printStackTrace();
            // Afficher un message d'erreur à l'utilisateur si nécessaire
            // ...
        }
    }
    @FXML
    void afficherEvenement(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvent.fxml"));

        try {
            Parent root = loader.load();
            AfficherListEvent ap = loader.getController();
            eventNameTextField.getScene().setRoot(root);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    void getNameToSearchWith(Event event){
        nameToSearchWith = event.getName();
        System.out.println("the word is :" + nameToSearchWith);
    }
    public void navigateToAfficherLesVoyages(ActionEvent event) {
        try {

            FXMLLoader loader = null;
            Parent root = loader.load(getClass().getResource("/VoyageListInterface.fxml"));
            Node sourceNode = (Node) event.getSource();
            Stage stage = (Stage) sourceNode.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}