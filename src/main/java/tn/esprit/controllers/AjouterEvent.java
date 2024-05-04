package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tn.esprit.models.Event;
import tn.esprit.services.EventService;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.scene.Parent;
 import  javafx.scene.control.Label;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.Date;



import java.awt.*;
import java.io.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterEvent {
    @FXML
    private Label CapacityErrorLabel;

    @FXML
    private Label DescriptionErrorLabel;

    @FXML
    private Label DestinationErrorLabel;

    @FXML
    private Label EndErrorLabel;

    @FXML
    private Label ReservedErrorLabel;

    @FXML
    private Label StartErrorLabel;

    @FXML
    private TextField URLImage;

    @FXML
    private TextField capacityTextField;

    @FXML
    private TextField destinationTextField;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextArea eventDescriptionTextArea;

    @FXML
    private ImageView eventImageView;

    @FXML
    private Label eventNameErrorLabel;

    @FXML
    private TextField eventNameTextField;

    @FXML
    private Label eventPriceErrorLabel;

    @FXML
    private TextField eventPrixTextField;

    @FXML
    private TextField reservedTextField;

    @FXML
    private DatePicker startDatePicker;
    private String filePath;

    @FXML
    void afficherEvenement(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventList.fxml"));

        try {
            Parent root = loader.load();
            AfficherListEvent ap = loader.getController();
            eventNameTextField.getScene().setRoot(root);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    @FXML
    void ajouterEvent(ActionEvent event) {
        // Valider les données saisies par l'utilisateur
        if (!validateInput()) {
            return; // Si la validation échoue, arrêtez l'exécution de la méthode
        }

        // Si la validation réussit, procédez à l'ajout de l'événement
        EventService es = new EventService();
        Event e = new Event();
        e.setName(eventNameTextField.getText());
        e.setDescription(eventDescriptionTextArea.getText());
        e.setCapacity(Integer.parseInt(capacityTextField.getText()));
        e.setReserved(Integer.parseInt(reservedTextField.getText()));
        e.setDate(startDatePicker.getValue().toString());
        e.setEnd(endDatePicker.getValue().toString());
        e.setPrix(Double.parseDouble(eventPrixTextField.getText()));
        e.setDestination(destinationTextField.getText());
        e.setImageFile(URLImage.getText());

        try {
            // Get the month and day values from the event date
            // Get the month and day values from the event date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Specify the date format
            java.sql.Date eventDate = new java.sql.Date(sdf.parse(e.getDate()).getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(eventDate);
            int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 because Calendar months are zero-based
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Check if there's already an event on the same day
            if (EventService.eventExistsOnDay(month, day)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Il existe déjà un événement pour ce jour.");
                alert.showAndWait();
                return; // Exit the method if event exists on the same day
            }

            // Count the events in the month
            int eventCount = EventService.countEventsInMonth(month);

            // Check if the event count exceeds 5
            if (eventCount >= 5) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Vous ne pouvez pas ajouter plus de 5 événements pour ce mois.");
                alert.showAndWait();
                return; // Exit the method if event count exceeds 5
            }
            es.ajouter(e);
            notiff();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("Event added successfully");
            alert.showAndWait();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void notiff() {
        Notifications notifications=Notifications.create();
        notifications.text("Event ajoutee");
        notifications.title("Success Message");
        notifications.hideAfter(Duration.seconds(4));
        /*notifications.darkStyle();*/
        /*   notifications.position(Pos.BOTTOM_CENTER);*/
        notifications.show();
    }

    void clearErrorLabels() {
        eventNameErrorLabel.setText("");
        DescriptionErrorLabel.setText("");
        StartErrorLabel.setText("");
        ReservedErrorLabel.setText("");
        DestinationErrorLabel.setText("");
        eventPriceErrorLabel.setText("");
        CapacityErrorLabel.setText("");
    }

    private boolean validateInput() {
        clearErrorLabels();
        boolean valid = true;

        // Check if the text is empty for nom_event
        if (eventNameTextField.getText().isEmpty()) {
            eventNameErrorLabel.setText("Le champ 'nom event' ne doit pas être vide.");
            valid = false;
        } else {
            String text = eventNameTextField.getText();
            Pattern pattern = Pattern.compile("[a-zA-Z0-9 ]+");
            Matcher matcher = pattern.matcher(text);

            if (!matcher.matches()) {
                eventNameErrorLabel.setText("Le champ 'nom event' ne doit contenir que des lettres et des chiffres.");
                eventNameTextField.setStyle("-fx-text-fill: red;");
                valid = false;
            }
        }

        // Check description
        if (eventDescriptionTextArea.getText().isEmpty()) {
            DescriptionErrorLabel.setText("Le champ 'description' ne doit pas être vide.");
            valid = false;
        }

        // Check dates
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            StartErrorLabel.setText("Les dates ne sont pas valides.");
            valid = false;
        }

        // Check reservedTextField
        try {
            int reserved = Integer.parseInt(reservedTextField.getText());
            if (reserved < 0) {
                ReservedErrorLabel.setText("Le nombre de réservation ne peut pas être négatif.");
                valid = false;
            }
        } catch (NumberFormatException e) {
            ReservedErrorLabel.setText("Le champ 'réservation' doit être un nombre entier.");
            valid = false;
        }

        // Check destinationTextField
        if (destinationTextField.getText().isEmpty()) {
            DestinationErrorLabel.setText("Le champ 'destination' ne doit pas être vide.");
            valid = false;
        }

        // Check eventPrixTextField
        try {
            double prix = Double.parseDouble(eventPrixTextField.getText());
            if (prix < 0) {
                eventPriceErrorLabel.setText("Le prix ne peut pas être négatif.");
                valid = false;
            }
        } catch (NumberFormatException e) {
            eventPriceErrorLabel.setText("Le champ 'prix' doit être un nombre décimal.");
            valid = false;
        }

        // Check capacityTextField
        try {
            int capacity = Integer.parseInt(capacityTextField.getText());
            if (capacity <= 0) {
                CapacityErrorLabel.setText("La capacité doit être un entier positif.");
                valid = false;
            }
        } catch (NumberFormatException e) {
            CapacityErrorLabel.setText("Le champ 'capacité' doit être un nombre entier.");
            valid = false;
        }

        return valid;
    }


    // Ajoutez d'autres validations selon vos besoins, par exemple, vérifier si les champs numériques contiennent des nombres val

    @FXML
    void chargerImage(ActionEvent event) throws FileNotFoundException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");

        // Set file extension filter to only allow image files
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());


        // Check if a file is selected and it's an image
        if (selectedFile != null && isImageFile(selectedFile)) {
            // Store the file path with forward slashes
            filePath = selectedFile.getAbsolutePath().replace("\\", "/");
            System.out.println("File path stored: " + filePath);

            // Set the image in the ImageView
            javafx.scene.image.Image image = new javafx.scene.image.Image(selectedFile.toURI().toString());
            eventImageView.setFitWidth(image.getWidth());
            eventImageView.setFitHeight(image.getHeight());
            eventImageView.setImage(image);
            URLImage.setText(filePath);

        } else {
            System.out.println("Please select a valid image file.");
        }
    }
    private boolean isImageFile(File file) {
        try {
            javafx.scene.image.Image image = new Image(file.toURI().toString());
            return image.isError() ? false : true;
        } catch (Exception e) {
            return false;
        }
    }
    public String getFilePath() {
        return filePath;
    }

    public void Clear(ActionEvent actionEvent) {
        eventNameTextField.setText("");
        eventDescriptionTextArea.setText("");
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        reservedTextField.setText("");
        destinationTextField.setText("");
        eventPrixTextField.setText("");
        capacityTextField.setText("");
        eventImageView.setImage(null);
    }
   }
