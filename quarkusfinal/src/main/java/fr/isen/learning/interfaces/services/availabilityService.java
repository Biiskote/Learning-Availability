
package fr.isen.learning.interfaces.services;


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
import fr.isen.learning.interfaces.models.availabilityModel;

public interface availabilityService {

    
    @Singleton
public class AvailabilityService {
    static AgroalDataSource dataSource = CDI.current().select(AgroalDataSource.class).get();

    public static List<availabilityModel> getAvailability() {
        Connection conn = null;
        List<availabilityModel> availabilities = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM formationavailability");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                availabilityModel availability = new availabilityModel();
                availability.setAvailabilityId(rs.getString("availabilityId"));
                availability.setFormationId(rs.getString("formationId"));
                availability.setYear(rs.getInt("year"));
                availability.setTimeline(rs.getString("timeline").toCharArray());
                availabilities.add(availability);
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return availabilities;
    }

    public static availabilityModel getAvailabilityById(String availabilityId) throws JsonProcessingException {
        List<availabilityModel> availabilities = getAvailability();
        for (availabilityModel availability : availabilities) {
            if (availability.getAvailabilityId().equals(availabilityId)) {
                return availability;
            }
        }
        throw new JsonProcessingException("La disponibilité avec l'ID " + availabilityId + " n'a pas été trouvée") {
        };
    }

    public static availabilityModel insertAvailability(String availabilityId,
                                                        String formationId,
                                                        int year,
                                                        String timeline) {
        availabilityModel availability = new availabilityModel();
        availability.setAvailabilityId(availabilityId);
        availability.setFormationId(formationId);
        availability.setYear(year);
        availability.setTimeline(timeline.toCharArray());
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO formationavailability (availabilityId, formationId, year, timeline) VALUES (?, ?, ?, ?)");
            stmt.setString(1, availabilityId);
            stmt.setString(2, formationId);
            stmt.setInt(3, year);
            stmt.setString(4, timeline);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion dans la base de données", e);
        }
        return availability;
    }

    public static availabilityModel updateAvailability(String availabilityId,
                                                        String formationId,
                                                        int year,
                                                        String timeline) {
        Connection conn = null;
        availabilityModel updatedAvailability = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE formationavailability SET formationId = ?, year = ?, timeline = ? WHERE availabilityId = ?");
            stmt.setString(1, formationId);
            stmt.setInt(2, year);
            stmt.setString(3, timeline);
            stmt.setString(4, availabilityId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                updatedAvailability = new availabilityModel();
                updatedAvailability.setAvailabilityId(availabilityId);
                updatedAvailability.setFormationId(formationId);
                updatedAvailability.setYear(year);
                updatedAvailability.setTimeline(timeline.toCharArray());
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la disponibilité", e);
        }
        return updatedAvailability;
    }

    public static void deleteAvailabilityById(String availabilityId) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM formationavailability WHERE availabilityId = ?");
            stmt.setString(1, availabilityId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aucune disponibilité avec l'ID " + availabilityId + " trouvée.");
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la disponibilité", e);
        }
    }
}
}
