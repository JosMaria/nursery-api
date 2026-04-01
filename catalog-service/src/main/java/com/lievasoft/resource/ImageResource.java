package com.lievasoft.resource;

import com.lievasoft.resource.validator.ImageValidator;
import com.lievasoft.service.ImageService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.net.URI;

@Path("/api/v1/plants")
public class ImageResource {

    private final ImageValidator imageValidator;
    private final ImageService imageService;

    public ImageResource(ImageValidator imageValidator, ImageService imageService) {
        this.imageValidator = imageValidator;
        this.imageService = imageService;
    }

    @POST
    @Path("/{id}/images")
    public Response save(@RestPath("id") Long plantId, @RestForm("file") FileUpload imageUpload) {
        imageValidator.validate(imageUpload);
        var plantImageResponse = imageService.persist(plantId, imageUpload);
        URI location = URI.create("/api/v1/plants/%s/images".formatted(plantId));
        return Response.created(location).entity(plantImageResponse).build();
    }

    @GET
    @Path("/{id}/images")
    public Response fetchImagePlantCard(@RestPath("id") Long plantId) {
        var downloadImageResponse = imageService.obtainImageToPlantCardBy(plantId);
        return Response.ok(downloadImageResponse.imageBytes())
                .header("Content-Type", downloadImageResponse.contentType())
                .build();
    }
}
