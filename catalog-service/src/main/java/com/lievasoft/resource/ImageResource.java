package com.lievasoft.resource;

import com.lievasoft.resource.validator.ImageValidator;
import com.lievasoft.service.DefaultImageService;
import com.lievasoft.service.ImageService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.net.URI;

@Path("/api/v1/plants")
public class ImageResource {

    private final ImageValidator imageValidator;
    private final DefaultImageService defaultImageService;
    private final ImageService imageService;

    public ImageResource(ImageValidator imageValidator, DefaultImageService defaultImageService, ImageService imageService) {
        this.imageValidator = imageValidator;
        this.defaultImageService = defaultImageService;
        this.imageService = imageService;
    }

    @POST
    @Path("/{id}/images")
    public Response save(@RestPath("id") Long plantId,
                         @RestQuery("selected") boolean isSelected,
                         @RestForm("file") FileUpload imageUpload) {
        imageValidator.validate(imageUpload);
        var plantImageResponse = defaultImageService.persist(plantId, isSelected, imageUpload);
        URI location = URI.create("/api/v1/plants/%s/images".formatted(plantId));
        return Response.created(location).entity(plantImageResponse).build();
    }

    @GET
    @Path("/{id}/images/card")
    public Response fetchImagePlantCard(@RestPath("id") Long plantId) {
        var downloadImageResponse = defaultImageService.obtainImageToPlantCardBy(plantId);
        return Response.ok(downloadImageResponse.imageBytes())
                .header("Content-Type", downloadImageResponse.contentType())
                .build();
    }

    @GET
    @Path("/{plantId}/images/selection")
    public Response fetchImagesPerPlantToSelection(long plantId) {
        var imageSelectionResponse = imageService.obtainImagesSummaryToSelect(plantId);
        return Response.ok(imageSelectionResponse).build();
    }

    @GET
    @Path("/{plantId}/images/{imageId}")
    public Response fetchImagePlant(long plantId, long imageId) {
        var downloadImageResponse = imageService.obtainImagePlantBy(plantId, imageId);
        return Response.ok(downloadImageResponse.imageBytes())
                .header("Content-Type", downloadImageResponse.contentType())
                .build();
    }

    @PATCH
    @Path("/{plantId}/images/{imageId}")
    public Response updateIsSelected(@RestPath("plantId") long plantId, @RestPath("imageId") long imageId) {
        boolean isChanged = imageService.setImageAsSelected(plantId, imageId);
        return Response.ok(isChanged).build();
    }
}
