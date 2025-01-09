package fr.isen.learning.interfaces.services;

import fr.isen.learning.interfaces.models.locationModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Singleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class locationService {
    static AgroalDataSource dataSource = CDI.current().select(AgroalDataSource.class).get();

    public static List<locationModel> Getlocation() {
        Connection conn = null;
        List<locationModel> locations = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * from location");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                locationModel locationtemp = new locationModel();
                locationtemp.setLocationId(rs.getString("locationId"));
                locationtemp.setName(rs.getString("name"));
                locationtemp.setAddressId(rs.getString("addressId"));
                locationtemp.setAvailability(rs.getInt("availability"));
                locations.add(locationtemp);
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return locations;
    }

    public static locationModel GetLocationById(String locationId) throws JsonProcessingException {
        List<locationModel> locations = Getlocation();
        for (locationModel locationtemp : locations) {
            if (locationtemp.getLocationId().equals(locationId)) {
                return locationtemp;
            }
        }
        throw new JsonProcessingException("La location avec l'id " + locationId + " n'a pas été trouvé") {
        };
    }

    public static locationModel InsertLocation(String locationId,
                                               String name,
                                               String addressId,
                                               int availability)
    {

        locationModel locationtemp = new locationModel();
        locationtemp.setLocationId(locationId);
        locationtemp.setName(name);
        locationtemp.setAddressId(addressId);
        locationtemp.setAvailability(availability);
        Connection conn = null;
        List<locationModel> locations = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO location (locationId, name, addressId, availability) VALUES (?, ?, ?, ?)");
            stmt.setString(1, locationId); // Premier
            stmt.setString(2, name);       // Deuxième
            stmt.setString(3, addressId);  // Troisième
            stmt.setInt(4, availability);  //Quatrième
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion dans la base de données, veuillez respecter le format suivant : localhost:8086/locations/[ID]/[NOM]/[ADRESSEID]/[AVAILABILITY] (Availability : 1 = Occupé 0 = Libre)");
        }
        return locationtemp;
    }
    public static locationModel UpdateLocation(String locationId,
                                               String name,
                                               String addressId,
                                               int availability) {
        Connection conn = null;
        locationModel updatedLocation = null;
        try {
            // Connexion à la base de données
            conn = dataSource.getConnection();

            // Préparer la requête SQL pour mettre à jour les données
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE location SET name = ?, addressId = ?, availability = ? WHERE locationId = ?"
            );

            // Remplir les paramètres de la requête
            stmt.setString(1, name);          // Nouveau nom
            stmt.setString(2, addressId);    // Nouvelle adresse
            stmt.setInt(3, availability);    // Nouvelle disponibilité
            stmt.setString(4, locationId);  // ID de l'emplacement à mettre à jour

            // Exécuter la mise à jour
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                updatedLocation = new locationModel();
                updatedLocation.setLocationId(locationId);
                updatedLocation.setName(name);
                updatedLocation.setAddressId(addressId);
                updatedLocation.setAvailability(availability);
            }

            // Fermer les ressources
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'élément avec ID : " + locationId, e);
        }

        return updatedLocation;
    }
    public static void deleteLocationById(String locationId) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            // Requête SQL pour supprimer la location
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM location WHERE locationId = ?");
            stmt.setString(1, locationId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aucune location avec l'ID " + locationId + " trouvée.");
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la location", e);
        }
    }
}

