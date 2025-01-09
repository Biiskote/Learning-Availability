package org.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.learning.interfaces.models.locationModel;
import fr.isen.learning.interfaces.services.locationService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;

@Path("/locations")
public class locationResource {
    @Inject
    locationService LocationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<locationModel> GetLocation() {
        return locationService.Getlocation();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public locationModel GetLocationById(@PathParam("id") String id) throws JsonProcessingException {
        return locationService.GetLocationById(id);
    }

    @PUT
    @Produces
    @Path("/{id}/{name}/{addressid}/{availability}")
    public Response InsertLocation(@PathParam("id") String id,
                                        @PathParam("name") String name,
                                        @PathParam("addressid") String addressid,
                                        @PathParam("availability") int availability) {
        try {
            // Appeler la méthode du service pour insérer les données
            locationModel insertedLocation = locationService.InsertLocation(id, name, addressid, availability);

            // Retourner une réponse 200 avec l'objet inséré
            return Response.status(Response.Status.OK).entity(insertedLocation).build();
        } catch (RuntimeException e) {
            // Gérer l'exception et retourner une réponse 500 avec un message d'erreur
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()) // Message d'erreur à afficher dans Postman
                    .type("text/plain")
                    .build();
        }
    }
    @POST
    @Produces
    @Path("/{id}/{name}/{addressid}/{availability}")
    public Response UpdateLocationByID(@PathParam("id") String id,
                                            @PathParam("name") String name,
                                            @PathParam("addressid") String addressid,
                                            @PathParam("availability") int availability) {
        try {
            // Appeler la méthode du service pour insérer une nouvelle location
            locationModel insertedLocation = locationService.UpdateLocation(id, name, addressid, availability);

            // Retourner une réponse 201 avec l'objet inséré
            return Response.status(Response.Status.CREATED).entity(insertedLocation).build();
        } catch (RuntimeException e) {
            // Si une erreur de runtime se produit, renvoyer une réponse 500 (Internal Server Error)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur interne du serveur : " + e.getMessage())
                    .build();
        } catch (Exception e) {
            // Gérer toute autre exception inattendue
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur inconnue : " + e.getMessage())
                    .build();
        }
    }
    @DELETE
    @Path("/{locationId}")
    public Response deleteLocation(@PathParam("locationId") String locationId){
        try {
            locationService.deleteLocationById(locationId);
            return Response.noContent().build(); // Réponse 204 No Content si la suppression a réussi
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Location with ID " + locationId + " not found.")
                    .build(); // Réponse 404 si une erreur se produit
        }
    }
}
