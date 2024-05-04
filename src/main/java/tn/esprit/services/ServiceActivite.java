package tn.esprit.services;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Activite;
import tn.esprit.models.Voyage;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ServiceActivite implements IService<Activite>{

    private Connection cnx;
    public ServiceActivite(){ cnx = MyDataBase.getInstance().getConnection();}
    public Activite saisie() {
        int id;
        String nom, description, type;
        LocalDateTime date;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Donner l'ID :");
        id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Nom :");
        nom = scanner.nextLine();
        System.out.println("Description :");
        description = scanner.nextLine();
        System.out.println("Type :");
        type = scanner.nextLine();

        date = LocalDateTime.of(2002, 2, 25, 0, 0, 0);

        return new Activite(id,nom,type,description,date);
    }
    @Override
    public void add(Activite activite){
        String qry = "INSERT INTO  `activite`(`nom`, `date`, `type`, `description`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);

            pstm.setString(1, activite.getNom());
            pstm.setTimestamp(2, Timestamp.valueOf(activite.getDate()));
            pstm.setString(3, activite.getType());
            pstm.setString(4, activite.getDescription());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void update(Activite activite){
        String qry = "UPDATE `activite` SET `nom`= ? ,`date`= ?,`type`= ?,`description`= ? WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);

            pstm.setString(1, activite.getNom());
            pstm.setTimestamp(2, Timestamp.valueOf(activite.getDate()));
            pstm.setString(3, activite.getType());
            pstm.setString(4, activite.getDescription());
            pstm.setInt(5, activite.getId());
            System.out.println("Activite modifié !");
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public boolean delete(int activiteID){
        String qry = "DELETE FROM `activite` WHERE `id` =?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, activiteID);
            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("L'activité à ID " + activiteID + " à été supprimé avec succès.");
                return true;
            } else {
                System.out.println("La suppression a échoué.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("La suppression a échoué: " + e.getMessage());
            return false;
        }
    }
    @Override
    public ArrayList<Activite> getAll(){
        //1-req SELECT
        //2-recuperation de la base de donné remplissage dans Array
        //3-retour du tableau done
        ArrayList<Activite> activites = new ArrayList<>();
        String qry ="SELECT * FROM `activite`";
        try {
            Statement stm = cnx.createStatement();

            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()){
                Activite a = new Activite();
                a.setId(rs.getInt(1));
                a.setNom(rs.getString("nom"));
                a.setDescription(rs.getString("description"));
                a.setType(rs.getString("type"));
                a.setDate(rs.getTimestamp("date").toLocalDateTime());
                activites.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return activites;
    }
    public void displayAll(ArrayList<Activite> items){
        for (Activite a : items) {
            System.out.println("ID: " + a.getId());
            System.out.println("Nom: " + a.getNom());
            System.out.println("Description: " + a.getDescription());
            System.out.println("Type: " + a.getType());
            System.out.println("Date : " + a.getDate());
            System.out.println();
        }
    }
    public static class ItemNotFoundException extends Exception {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }
    public ArrayList<Activite> findByNom(String nom) throws ServiceActivite.ItemNotFoundException {
        ArrayList<Activite> liste_des_activites = this.getAll();
        ArrayList<Activite> found_items = new ArrayList<>();
        Iterator<Activite> itr = liste_des_activites.iterator();
        while (itr.hasNext()) {
            Activite activite = itr.next();
            if (activite.getNom().toLowerCase().contains(nom)) {
                found_items.add(activite);
            }
        }
        if(found_items.isEmpty()){
            throw new ServiceActivite.ItemNotFoundException("L'activité du nom " + nom + " n'existe pas.");
        }
        return found_items;
    }

    public ArrayList<Activite> trierParType_nom(){
        Comparator<Activite> byType = new Comparator<Activite>() {
            @Override
            public int compare(Activite o1, Activite o2) {return o1.getType().compareTo(o2.getType());}
        };
        Comparator<Activite> byName = Comparator.comparing(Activite::getNom);
        ArrayList<Activite> activites = this.getAll();

        activites.sort(byType.thenComparing(byName));
        return activites;
    }
}
