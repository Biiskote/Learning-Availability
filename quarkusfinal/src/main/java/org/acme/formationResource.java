package org.acme;


import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.learning.interfaces.models.enums.FORMATIONSTATE;
import fr.isen.learning.interfaces.models.formationModel;
import fr.isen.learning.interfaces.services.formationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;



@Path("/formations")
public class formationResource {
    @Inject
    formationService FormationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<formationModel> GetFormations() {
        return formationService.GetFormations();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public formationModel GetFormationById(@PathParam("id") String id) throws JsonProcessingException {
        return formationService.GetFormationById(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/{name}/{duration}/{startDate}/{endDate}/{locationId}/{state}")
    public Response InsertFormation(
            @PathParam("id") String id,
            @PathParam("name") String name,
            @PathParam("duration") int duration,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate,
            @PathParam("locationId") String locationId,
            @PathParam("state") String state
    ) {
        try {
            formationModel insertedFormation = formationService.InsertFormation(
                    id, name, duration,
                    java.sql.Date.valueOf(startDate),
                    java.sql.Date.valueOf(endDate),
                    locationId,
                    FORMATIONSTATE.valueOf(state)
            );

            return Response.status(Response.Status.OK).entity(insertedFormation).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .type("text/plain")
                    .build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/{name}/{duration}/{startDate}/{endDate}/{locationId}/{state}")
    public Response UpdateFormation(
            @PathParam("id") String id,
            @PathParam("name") String name,
            @PathParam("duration") int duration,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate,
            @PathParam("locationId") String locationId,
            @PathParam("state") String state
    ) {
        try {
            formationModel updatedFormation = formationService.UpdateFormation(
                    id, name, duration,
                    java.sql.Date.valueOf(startDate),
                    java.sql.Date.valueOf(endDate),
                    locationId,
                    FORMATIONSTATE.valueOf(state)
            );

            return Response.status(Response.Status.CREATED).entity(updatedFormation).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur interne du serveur : " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response DeleteFormation(@PathParam("id") String id) {
        try {
            formationService.DeleteFormationById(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Formation with ID " + id + " not found.")
                    .build();
        }
    }
}
