package tn.esprit.services;
import tn.esprit.models.Event;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventService implements IService<Event> {

    private static Connection connection;
public EventService(){
    connection= MyDataBase.getInstance().getConnection();
}

    public static Event getEventById(int eventId) {
        Event event = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = MyDataBase.getInstance().getConnection();
            String query = "SELECT * FROM event WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, eventId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve event details from the result set
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("prix");

                // Create an Event object with the retrieved data
                event = new Event(id, name, price);
            }
        } catch (SQLException ex) {
            // Log the exception or handle it as needed
            ex.printStackTrace();
        } finally {
            // Close the result set, statement, and connection
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                // Log the exception or handle it as needed
                e.printStackTrace();
            }
        }

        return event;
    }

    public static int countEventsInMonth(int month) throws SQLException {
        String query = "SELECT COUNT(*) AS event_count FROM event WHERE MONTH(date) = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, month);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("event_count");
                }
            }
        }
        return 0; // Return 0 if no events found for the month
    }

    public static boolean eventExistsOnDay(int month, int day) throws SQLException {
        String query = "SELECT COUNT(*) AS event_count FROM event WHERE MONTH(date) = ? AND DAY(date) = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, month);
            statement.setInt(2, day);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int eventCount = resultSet.getInt("event_count");
                    return eventCount > 0; // Return true if there is at least one event on the given day
                }
            }
        }
        return false; // Return false if no event found on the given day
    }

    @Override
    public void ajouter(Event e) throws SQLException {
        try {
           String req = "INSERT INTO event(id, name, description, capacity, reserved, date, end, prix, destination, image_file) " +
                    "VALUES(" + e.getId() + ", '" + e.getName() + "', '" + e.getDescription() + "', " + e.getCapacity() + ", " +
                    e.getReserved() + ", '" + e.getDate() + "', '" + e.getEnd() +  "', " + e.getPrix() + ", '" + e.getDestination() + "', '" +
                    e.getImageFile() + "')";



            Statement st = connection.createStatement();
            st.executeUpdate(req);
            System.out.println("Événement ajouté avec succès");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }



    @Override
    public void modifier( Event event) throws SQLException {
        try {
            String req = "UPDATE event SET name=?, description=?, capacity=?, reserved=?, date=?, end=?, prix=?, destination=?, image_file=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(req);

            ps.setString(1, event.getName());
            ps.setString(2, event.getDescription());
            ps.setInt(3, event.getCapacity());
            ps.setInt(4, event.getReserved());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            ps.setDate(5, new java.sql.Date(sdf.parse(event.getDate()).getTime()));
            ps.setDate(6, new java.sql.Date(sdf.parse(event.getEnd()).getTime()));
            ps.setDouble(7, event.getPrix());
            ps.setString(8, event.getDestination());
            ps.setString(9, event.getImageFile());
            ps.setInt(10, event.getId());

            ps.executeUpdate();
            System.out.println("Événement modifié avec succès");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void supprimer(int id) {
        try {
            Statement st = connection.createStatement();
            String req = "DELETE FROM event WHERE id = " + id;
            st.executeUpdate(req);
            System.out.println("L'événement avec l'id = " + id + " a été supprimé avec succès.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public ArrayList<Event> recuperer() {
        ArrayList<Event> events = new ArrayList<>();
        String req = "SELECT * FROM event";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/codeshift", "root", "");
             PreparedStatement statement = connection.prepareStatement(req);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setName(rs.getString("name"));
                event.setDescription(rs.getString("description"));
                event.setCapacity(rs.getInt("capacity"));
                event.setReserved(rs.getInt("reserved"));
                event.setDate(rs.getString("date"));
                event.setEnd(rs.getString("end"));
                event.setDestination(rs.getString("destination"));
                event.setPrix(rs.getDouble("prix"));
                event.setImageFile(rs.getString("image_file"));

                events.add(event);
            }
        } catch (SQLException ex) {
            // Log the error using a logging framework
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return events;
    }



    public ArrayList<Event> chercherEvent(String name) {
        ArrayList<Event> events = new ArrayList<>();
        try {
            String query = "SELECT * FROM event WHERE name =?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Event event = new Event();
                event.setId(resultSet.getInt("id"));
                event.setName(resultSet.getString("name"));
                event.setDescription(resultSet.getString("description"));
                event.setCapacity(resultSet.getInt("capacity"));
                event.setReserved(resultSet.getInt("reserved"));
                event.setDate(String.valueOf(resultSet.getDate("date")));
                event.setEnd(String.valueOf(resultSet.getDate("end")));
                event.setPrix(resultSet.getDouble("prix"));
                event.setDestination(resultSet.getString("destination"));
                event.setImageFile(resultSet.getString("image_file"));
                events.add(event);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return events;
    }



    public Event getById(int id) {
            Event event = null;
            try {
                String req = "SELECT * FROM event WHERE id = ?";
                PreparedStatement ps = connection.prepareStatement(req);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    event = new Event();
                    event.setId(rs.getInt("id"));
                    event.setName(rs.getString("name"));
                    event.setDescription(rs.getString("description"));
                    event.setCapacity(rs.getInt("capacity"));
                    event.setReserved(rs.getInt("reserved"));
                    event.setDate(rs.getString("date"));
                    event.setEnd(rs.getString("end"));
                    event.setPrix(rs.getDouble("prix"));
                    event.setDestination(rs.getString("destination"));
                    event.setImageFile(rs.getString("image_file"));
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            return event;
        }

    public boolean isSoldOut(Event event) {
        return event.getReserved() >= event.getCapacity();
    }
}

