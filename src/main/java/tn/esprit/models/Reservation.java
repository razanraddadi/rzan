package tn.esprit.models;

import java.time.LocalDate;

public class Reservation {

    private int id;
    private int numberOfReservations;
    private String paymentMode;
    private String message;
    private String type;
    private int idevent;
    private LocalDate date;

    private double prix_e;
    private double prix_total;
    private String eventName;


    public Reservation(int id, int numberOfReservations, String paymentMode, String message, String type, int idevent, double prix_e, double prix_total, String eventName, LocalDate date) {
        this.id = id;
        this.numberOfReservations = numberOfReservations;
        this.paymentMode = paymentMode;
        this.message = message;
        this.type = type;
        this.idevent = idevent;
        this.prix_e = prix_e;
        this.prix_total = prix_total;
        this.eventName = eventName;
        this.date = date;
    }

    public Reservation() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrix_e() {
        return prix_e;
    }

    public void setPrix_e(double prix_e) {
        this.prix_e = prix_e;
    }

    public double getPrix_total() {
        prix_total = prix_e * numberOfReservations;
        return prix_total;
    }

    public void setPrix_total(double prix_total) {
        this.prix_total = prix_total;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfReservations() {
        return numberOfReservations;
    }

    public void setNumberOfReservations(int numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdevent() {
        return idevent;
    }

    public void setIdevent(int idevent) {
        this.idevent = idevent;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", numberOfReservations=" + numberOfReservations +
                ", paymentMode='" + paymentMode + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", idevent=" + idevent +
                ", date=" + date +
                ", prix_e=" + prix_e +
                ", prix_total=" + prix_total +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}