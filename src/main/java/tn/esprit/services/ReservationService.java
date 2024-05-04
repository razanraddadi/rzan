package tn.esprit.services;

import tn.esprit.models.Event;
import tn.esprit.models.Reservation;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationService implements IService<Reservation> {
    private static Connection cnx;

    public ReservationService() {
        cnx = MyDataBase.getInstance().getConnection();
    }
    public static double calculateTotalIncome(ArrayList<Reservation> reservations) {
        String sql = "SELECT SUM(prix_total) FROM reservation";
        double totalIncome = 0.0;
        try (PreparedStatement statement = cnx.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalIncome = resultSet.getDouble(1);
            }
    } catch (SQLException e) {
            throw new RuntimeException(e);
        } return totalIncome;}

    public static double calculateTotalIncomeToday(ArrayList<Reservation> reservations) {
        String sql = "SELECT SUM(prix_total) FROM reservation WHERE date = ?";
        double totalIncome = 0.0;

        try (PreparedStatement statement = cnx.prepareStatement(sql)) {

            LocalDate today = LocalDate.now();
            statement.setDate(1, java.sql.Date.valueOf(today));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    totalIncome = resultSet.getDouble(1);
                }
            }
        } catch (SQLException e) {

        }

        return totalIncome;
    }
    @Override
    public void ajouter(Reservation reservation) throws SQLException {
        Connection connection = MyDataBase.getInstance().getConnection();
        String req = "INSERT INTO reservation(id, nbrreservation, modepaiement, message, type, idevent_id, event_name, prix_e, prix_total,date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, reservation.getId());
            ps.setInt(2, reservation.getNumberOfReservations());
            ps.setString(3, reservation.getPaymentMode());
            ps.setString(4, reservation.getMessage());
            ps.setString(5, reservation.getType());
            ps.setInt(6, reservation.getIdevent());
            ps.setString(7, reservation.getEventName());
            ps.setDouble(8, reservation.getPrix_e());
            ps.setDouble(9, reservation.getPrix_total());
            ps.setDate(10, java.sql.Date.valueOf(reservation.getDate()));
            ps.executeUpdate();
            System.out.println("Réservation ajoutée avec succès");
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de la réservation : " + ex.getMessage());
            // Optionally, you can log the exception or perform other error handling here
            throw ex; // Rethrow the exception if needed

            // Do not close the connection here
            // You can keep the connection open for future use
        }
    }


    @Override
    public void modifier(Reservation reservation) {
            String qry = "UPDATE `reservation` SET `nbrreservation`=?, `modepaiement`=?, `message`=?, `type`=?, `idevent_id`=?, `event_name`=?, `prix_e`=?, `prix_total`=?, `date`=? WHERE `id`=?";
            try {
                PreparedStatement pstm = cnx.prepareStatement(qry);

                pstm.setInt(1, reservation.getNumberOfReservations());
                pstm.setString(2, reservation.getPaymentMode());
                pstm.setString(3, reservation.getMessage());
                pstm.setString(4, reservation.getType());
                pstm.setInt(5, reservation.getIdevent());
                pstm.setString(6, reservation.getEventName());
                pstm.setDouble(7, reservation.getPrix_e());
                pstm.setDouble(8, reservation.getPrix_total());
                pstm.setDate(9, java.sql.Date.valueOf(java.time.LocalDate.now()));
                pstm.setInt(10, reservation.getId());
                System.out.println("Réservation modifiée !");

                pstm.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    public void addReservation(Event event, Reservation reservation) {
        event.setReserved(event.getReserved() + reservation.getNumberOfReservations());}

    @Override
    public void supprimer(int id) throws SQLException {
        try (Connection connection = MyDataBase.getInstance().getConnection()) {
            String req = "DELETE FROM reservation WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("La réservation avec l'id = " + id + " a été supprimée avec succès.");
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de la suppression de la réservation : " + ex.getMessage());
        }
    }

    @Override

    public ArrayList<Reservation> recuperer() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation ";

        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);





            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));
                reservation.setNumberOfReservations(rs.getInt("nbrreservation"));
                reservation.setPaymentMode(rs.getString("modepaiement"));
                reservation.setMessage(rs.getString("message"));
                reservation.setType(rs.getString("type"));
                reservation.setIdevent(rs.getInt("idevent_id"));
                reservation.setEventName(rs.getString("event_name"));
                reservation.setPrix_e(rs.getDouble("prix_e"));
                reservation.setPrix_total(rs.getDouble("prix_total"));

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, e);
        }

        return reservations;
    }
}