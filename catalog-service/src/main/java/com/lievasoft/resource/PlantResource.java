package com.lievasoft.resource;

import com.lievasoft.dto.request.plant.PlantCreateDTO;
import com.lievasoft.dto.response.plant.PlantSummaryResponse;
import com.lievasoft.resource.validator.PlantValidator;
import com.lievasoft.service.PlantService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import java.net.URI;

@Path("/api/v1/plants")
public class PlantResource {

    private final PlantService plantService;
    private final PlantValidator plantValidator;

    public PlantResource(PlantService plantService, PlantValidator plantValidator) {
        this.plantService = plantService;
        this.plantValidator = plantValidator;
    }

    @POST
    public Response save(PlantCreateDTO plantCreateDTO) {
        plantValidator.validate(plantCreateDTO);
        var plantCreateResponse = plantService.create(plantCreateDTO);
        URI location = URI.create("/api/v1/plants/" + plantCreateResponse.id());
        return Response.created(location).entity(plantCreateResponse).build();
    }

    @GET
    public Response fetchPlantSummaries() {
        var plantSummariesResponse = plantService.obtainPlantSummaries();
        return Response.ok(plantSummariesResponse).build();
    }

    @GET
    @Path("/cards")
    public Response fetchPlantCards(@RestQuery("page") int numberPage,
                                    @RestQuery("size") @DefaultValue("8") int sizePage) {
        var plantCardsResponse = plantService.obtainPlantCards(numberPage, sizePage);
        return Response.ok(plantCardsResponse).build();
    }

    @GET
    @Path("/{id}/details")
    public Response fetchPlantDetailsById(@RestPath("id") Long plantId) {
        var plantDetailsResponse = plantService.obtainPlantDetailsById(plantId);
        return Response.ok(plantDetailsResponse).build();
    }

    @PATCH
    @Path("/{id}/favorite")
    public Response updateIsFavorite(@RestPath("id") Long plantId, @RestQuery("favorite") boolean isFavorite) {
        boolean isChanged = plantService.changeIsFavorite(plantId, isFavorite);
        return Response.ok(isChanged).build();
    }
}
