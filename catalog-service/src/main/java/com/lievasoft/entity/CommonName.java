package com.lievasoft.entity;

import com.lievasoft.dto.request.plant.CommonNameCreateDTO;
import com.lievasoft.dto.mapping.CommonNameToPlantDetails;
import jakarta.persistence.*;

import static com.lievasoft.plant.PlantConstant.FETCH_COMMON_NAME_TO_PLANT_DETAILS;
import static com.lievasoft.plant.PlantConstant.FETCH_COMMON_NAME_TO_PLANT_DETAILS_QUERY;

@Entity
@Table(name = "common_names")
@NamedNativeQuery(
        name = FETCH_COMMON_NAME_TO_PLANT_DETAILS,
        query = FETCH_COMMON_NAME_TO_PLANT_DETAILS_QUERY,
        resultSetMapping = "CommonNameToPlantDetailsMapping"
)
@SqlResultSetMapping(
        name = "CommonNameToPlantDetailsMapping",
        classes = @ConstructorResult(
                targetClass = CommonNameToPlantDetails.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "country", type = String.class),
                        @ColumnResult(name = "place", type = String.class)
                }
        )
)
public class CommonName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Country country;

    private String place;

    @Column(name = "is_selected")
    private boolean isSelected;

    public CommonName() {
    }

    public CommonName(CommonNameCreateDTO commonNameCreateDTO) {
        this.name = commonNameCreateDTO.name();
        this.country = commonNameCreateDTO.country();
        this.place = commonNameCreateDTO.place();
        this.isSelected = commonNameCreateDTO.isSelected();
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
