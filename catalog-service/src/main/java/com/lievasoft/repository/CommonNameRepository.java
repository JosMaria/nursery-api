package com.lievasoft.repository;

import com.lievasoft.dto.mapping.CommonNameToPlantDetails;
import com.lievasoft.entity.CommonName;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import static com.lievasoft.plant.PlantConstant.FETCH_COMMON_NAME_TO_PLANT_DETAILS;

@ApplicationScoped
public class CommonNameRepository implements PanacheRepository<CommonName> {

    public List<CommonNameToPlantDetails> fetchCommonNameToPlantDetails(Long plantId) {
        return getEntityManager()
                .createNamedQuery(FETCH_COMMON_NAME_TO_PLANT_DETAILS, CommonNameToPlantDetails.class)
                .setParameter("id", plantId)
                .getResultList();
    }
}
