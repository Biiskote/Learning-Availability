package fr.isen.learning.interfaces.services;

import fr.isen.learning.interfaces.models.formationModel;
import fr.isen.learning.interfaces.models.locationModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Singleton;
import java.util.Date;
import fr.isen.learning.interfaces.models.enums.FORMATIONSTATE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class formationService {
     static AgroalDataSource dataSource = CDI.current().select(AgroalDataSource.class).get();

     public static List<formationModel> GetFormations() {
        Connection conn = null;
        List<formationModel> formations = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM formation");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                formationModel formationTemp = new formationModel();
                formationTemp.setFormationId(rs.getString("formationId"));
                formationTemp.setName(rs.getString("name"));
                formationTemp.setDurationinHours(rs.getInt("durationInHours"));
                formationTemp.setStartDate(rs.getDate("startDate"));
                formationTemp.setEndDate(rs.getDate("endDate"));
                formationTemp.setLocationId(rs.getString("locationId"));
                formationTemp.setState(FORMATIONSTATE.valueOf(rs.getString("state")));
                formations.add(formationTemp);
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return formations;
    }

    public static formationModel GetFormationById(String formationId) throws JsonProcessingException {
        List<formationModel> formations = GetFormations();
        for (formationModel formationTemp : formations) {
            if (formationTemp.getFormationId().equals(formationId)) {
                return formationTemp;
            }
        }
        throw new JsonProcessingException("La formation avec l'id " + formationId + " n'a pas été trouvée") {
        };
    }

    public static formationModel InsertFormation(String formationId,
                                                 String name,
                                                 int durationInHours,
                                                 Date startDate,
                                                 Date endDate,
                                                 String locationId,
                                                 FORMATIONSTATE state) {

        formationModel formationTemp = new formationModel();
        formationTemp.setFormationId(formationId);
        formationTemp.setName(name);
        formationTemp.setDurationinHours(durationInHours);
        formationTemp.setStartDate(startDate);
        formationTemp.setEndDate(endDate);
        formationTemp.setLocationId(locationId);
        formationTemp.setState(state);
        
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO formation (formationId, name, durationInHours, startDate, endDate, locationId, state) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, formationId);
            stmt.setString(2, name);
            stmt.setInt(3, durationInHours);
            stmt.setDate(4, new java.sql.Date(startDate.getTime()));
            stmt.setDate(5, new java.sql.Date(endDate.getTime()));
            stmt.setString(6, locationId);
            stmt.setString(7, state.name());
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion dans la table formation");
        }
        return formationTemp;
    }

    public static formationModel UpdateFormation(String formationId,
                                                 String name,
                                                 int durationInHours,
                                                 Date startDate,
                                                 Date endDate,
                                                 String locationId,
                                                 FORMATIONSTATE state) {
        Connection conn = null;
        formationModel updatedFormation = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE formation SET name = ?, durationInHours = ?, startDate = ?, endDate = ?, locationId = ?, state = ? WHERE formationId = ?"
            );

            stmt.setString(1, name);
            stmt.setInt(2, durationInHours);
            stmt.setDate(3, new java.sql.Date(startDate.getTime()));
            stmt.setDate(4, new java.sql.Date(endDate.getTime()));
            stmt.setString(5, locationId);
            stmt.setString(6, state.name());
            stmt.setString(7, formationId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                updatedFormation = new formationModel();
                updatedFormation.setFormationId(formationId);
                updatedFormation.setName(name);
                updatedFormation.setDurationinHours(durationInHours);
                updatedFormation.setStartDate(startDate);
                updatedFormation.setEndDate(endDate);
                updatedFormation.setLocationId(locationId);
                updatedFormation.setState(state);
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la formation avec ID : " + formationId, e);
        }

        return updatedFormation;
    }

    public static void DeleteFormationById(String formationId) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM formation WHERE formationId = ?");
            stmt.setString(1, formationId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aucune formation avec l'ID " + formationId + " trouvée.");
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la formation", e);
        }
    }
}





