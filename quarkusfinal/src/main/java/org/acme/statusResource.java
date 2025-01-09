package org.acme;
//Mettez le nom et le chemin de votre dossier

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;



@Path("/status")
public class statusResource {
    AgroalDataSource dataSource = CDI.current().select(AgroalDataSource.class).get();
    @GET
    public String getStatus() throws JsonProcessingException {
        Connection conn = null;
        int count = 0;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT \n" +
                    "    (SELECT COUNT(*) FROM formation) +\n" +
                    "    (SELECT COUNT(*) FROM formationavailability) +\n" +
                    "    (SELECT COUNT(*) FROM location) AS total_row_count;\n");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // USE factory here
                count = rs.getInt(1);
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            String state = "OK";
        // OK ou KO ou Dégradé
        // L'état dégradé veut dire que votre code interne fonctionne mais que vous attendez un code dont vous êtes dépendants, donc d'un autre groupe
        String version = "1.0";
        // Remplacez par une requête réelle pour compter les éléments de votre BDD
        Map<String, Object> statusResponse = new HashMap<>();
        statusResponse.put("state", state);
        statusResponse.put("count", count);
        statusResponse.put("version", version);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(statusResponse);
    }

}