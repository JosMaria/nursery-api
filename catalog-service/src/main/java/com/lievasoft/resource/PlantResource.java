package com.lievasoft.resource;

import com.lievasoft.dto.plant.CommonNameCreateDTO;
import com.lievasoft.dto.plant.PlantCreateDTO;
import com.lievasoft.resource.validator.PlantValidator;
import com.lievasoft.service.PlantService;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;

import java.net.URI;
import java.util.Collection;

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
    @Path("/cards")
    public Response fetchPlantCards() {
        var plantCardsResponse = plantService.obtainPlantCards();
        return Response.ok(plantCardsResponse).build();
    }

    @GET
    @Path("/{id}/details")
    public Response fetchPlantDetailsById(@RestPath("id") Long plantId) {
        var plantDetailsResponse = plantService.obtainPlantDetailsById(plantId);
        return Response.ok(plantDetailsResponse).build();
    }

    @POST
    @Path("common-names")
    public Response createCommonNames(Collection<CommonNameCreateDTO> commonNameCreateDTO) {
        plantValidator.validateCommonNamesDTO(commonNameCreateDTO);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@RestPath("id") Long plantId) {
        var plantResponse = plantService.removeById(plantId);
        return Response.ok(plantResponse).build();
    }
}
