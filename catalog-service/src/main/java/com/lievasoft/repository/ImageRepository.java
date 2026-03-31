package com.lievasoft.repository;

import com.lievasoft.dto.response.ImageCardResponse;
import com.lievasoft.entity.Image;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ImageRepository implements PanacheRepository<Image> {

    public Optional<ImageCardResponse> fetchImageCard(Long plantId) {
        var maybeImageCard = getEntityManager()
                .createNamedQuery("Image.fetchImageCard", ImageCardResponse.class)
                .setParameter("id", plantId)
                .getSingleResult();

        return Optional.ofNullable(maybeImageCard);
    }
}
