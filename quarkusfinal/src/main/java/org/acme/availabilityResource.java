package org.acme;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.isen.learning.interfaces.models.availabilityModel;
import fr.isen.learning.interfaces.services.availabilityService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;


import jakarta.ws.rs.Path;

@Path("/availabilities")
public class availabilityResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<availabilityModel> getAllAvailabilities() {
        return availabilityService.AvailabilityService.getAvailability();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailabilityById(@PathParam("id") String id) {
        try {
            availabilityModel availability = availabilityService.AvailabilityService.getAvailabilityById(id);
            return Response.ok(availability).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Availability with ID " + id + " not found.")
                    .build();
        }
    }

    @PUT
    @Path("/{availabilityId}/{formationId}/{year}/{timeline}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertAvailability(@PathParam("availabilityId") String availabilityId,
                                       @PathParam("formationId") String formationId,
                                       @PathParam("year") int year,
                                       @PathParam("timeline") String timeline) {
        try {
            availabilityModel newAvailability = availabilityService.AvailabilityService.insertAvailability(availabilityId, formationId, year, timeline);
            return Response.status(Response.Status.CREATED).entity(newAvailability).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error inserting availability: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/{availabilityId}/{formationId}/{year}/{timeline}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAvailability(@PathParam("availabilityId") String availabilityId,
                                       @PathParam("formationId") String formationId,
                                       @PathParam("year") int year,
                                       @PathParam("timeline") String timeline) {
        try {
            availabilityModel updatedAvailability = availabilityService.AvailabilityService.updateAvailability(availabilityId, formationId, year, timeline);
            if (updatedAvailability != null) {
                return Response.ok(updatedAvailability).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Availability with ID " + availabilityId + " not found.")
                        .build();
            }
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating availability: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAvailability(@PathParam("id") String id) {
        try {
            availabilityService.AvailabilityService.deleteAvailabilityById(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Availability with ID " + id + " not found.")
                    .build();
        }
    }
}
