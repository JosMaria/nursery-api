package com.lievasoft.entity;

import com.lievasoft.dto.plant.PlantCreateDTO;
import com.lievasoft.dto.plant.PlantTaxonomy;
import com.lievasoft.dto.response.PlantCardResponse;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.lievasoft.plant.PlantConstant.*;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "plants")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = FETCH_PLANT_CARDS,
                query = FETCH_PLANT_CARDS_QUERY,
                resultSetMapping = "PlantCardsMapping"
        ),
        @NamedNativeQuery(
                name = FETCH_PLANT_TAXONOMY,
                query = FETCH_PLANT_TAXONOMY_QUERY,
                resultSetMapping = "PlantTaxonomyMapping"
        )
})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "PlantCardsMapping",
                classes = @ConstructorResult(
                        targetClass = PlantCardResponse.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "scientific_name", type = String.class),
                                @ColumnResult(name = "common_name", type = String.class),
                                @ColumnResult(name = "price", type = BigDecimal.class),
                                @ColumnResult(name = "storage_path", type = String.class),
                                @ColumnResult(name = "filename", type = String.class)
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "PlantTaxonomyMapping",
                classes = @ConstructorResult(
                        targetClass = PlantTaxonomy.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "scientific_name", type = String.class),
                                @ColumnResult(name = "price", type = BigDecimal.class),
                                @ColumnResult(name = "updated_at", type = LocalDateTime.class),
                                @ColumnResult(name = "division", type = String.class),
                                @ColumnResult(name = "class", type = String.class),
                                @ColumnResult(name = "taxonomic_order", type = String.class),
                                @ColumnResult(name = "family", type = String.class),
                                @ColumnResult(name = "genus", type = String.class),
                                @ColumnResult(name = "species", type = String.class)
                        }
                )
        )
})
public class Plant {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "plant_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "scientific_name", unique = true)
    private String scientificName;

    @JoinColumn(name = "taxonomy_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Taxonomy taxonomy;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.PERSIST)
    private final Set<CommonName> commonNames = new HashSet<>();

    @OneToMany(mappedBy = "plant", cascade = CascadeType.PERSIST)
    private final Set<Image> images = new HashSet<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Plant() {
    }

    public Plant(PlantCreateDTO plantCreateDTO) {
        this.scientificName = plantCreateDTO.scientificName();
        this.price = plantCreateDTO.price();
    }

    public Long getId() {
        return this.id;
    }

    public String getScientificName() {
        return this.scientificName;
    }

    public void setTaxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
        taxonomy.setPlant(this);
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @PrePersist
    public void onCreated() {
        var localDateTime = LocalDateTime.now();
        this.createdAt = localDateTime;
        this.updatedAt = localDateTime;
    }

    @PreUpdate
    public void onUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addCommonNames(Set<CommonName> commonNames) {
        commonNames.forEach(commonName -> {
            this.commonNames.add(commonName);
            commonName.setPlant(this);
        });
    }

    public void addImage(Image image) {
        this.images.add(image);
        image.setPlant(this);
    }
}
