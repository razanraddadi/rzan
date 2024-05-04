module PIDEV.voyage {

    requires javafx.fxml;
    requires javafx.controls;
    requires okhttp3;
    requires com.google.gson;
    requires java.sql;
    requires java.datatransfer;
    requires java.desktop;
    requires json.simple;
    requires AnimateFX;
    requires org.controlsfx.controls;
    requires org.apache.pdfbox;

    opens tn.esprit.controllers;
}