package com.lievasoft.entity;

import com.lievasoft.dto.request.plant.TaxonomyCreateDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "taxonomies")
public class Taxonomy {

    @Id
    private Long id;

    @MapsId
    @OneToOne(mappedBy = "taxonomy")
    private Plant plant;

    private String kingdom;
    private String division;

    @Column(name = "class")
    private String clazz;

    @Column(name = "taxonomic_order")
    private String order;

    private String family;
    private String genus;
    private String species;

    public Taxonomy() {
    }

    public Taxonomy(TaxonomyCreateDTO taxonomyCreateDTO) {
        this.kingdom = taxonomyCreateDTO.kingdom();
        this.division = taxonomyCreateDTO.division();
        this.clazz = taxonomyCreateDTO.clazz();
        this.order = taxonomyCreateDTO.order();
        this.family = taxonomyCreateDTO.family();
        this.genus = taxonomyCreateDTO.genus();
        this.species = taxonomyCreateDTO.species();
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
