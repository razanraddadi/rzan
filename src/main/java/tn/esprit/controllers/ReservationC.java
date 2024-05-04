package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import tn.esprit.models.Reservation;
import tn.esprit.services.MyListener;
import tn.esprit.services.MyListenerR;
import javafx.scene.image.Image;


import java.io.File;

public class ReservationC {
    @FXML
    private ImageView img;

    @FXML
    private Label name;

    @FXML
    private Label payment;

    @FXML
    private Label prix;
    private Reservation reservation;
    private MyListenerR myListener;
    public void initData(Reservation reservation, MyListenerR myListener) {

        this.reservation = reservation;
        this.myListener = myListener;
        name.setText(reservation.getEventName());
        payment.setText(reservation.getPaymentMode());
        prix.setText(String.valueOf(reservation.getPrix_total()));



    }


    @FXML
    void click(MouseEvent event) {
        myListener.onClickListenerR(reservation);

    }




}
