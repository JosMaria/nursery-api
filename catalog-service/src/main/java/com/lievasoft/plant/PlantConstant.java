package com.lievasoft.plant;

public class PlantConstant {

    public static final String FETCH_PLANT_CARDS = "Plant.fetchPlantCards";
    public static final String FETCH_PLANT_CARDS_QUERY = """
                    SELECT p.id, p.scientific_name, p.is_favorite, cn.name AS common_name, p.price, i.storage_path, i.filename
                    FROM plants p
                    LEFT JOIN (
                        SELECT DISTINCT ON (plant_id) plant_id, storage_path, filename
                        FROM images
                    ) AS i ON i.plant_id = p.id
                    LEFT JOIN (
                        SELECT plant_id, name
                        FROM common_names
                        WHERE is_selected = TRUE
                    ) AS cn ON cn.plant_id = p.id
                    ORDER BY p.is_favorite DESC
                    LIMIT :limit
                    OFFSET :offset
            """;

    public static final String FETCH_IMAGE_PLANT_CARD = "Image.fetchImageCard";
    public static final String FETCH_IMAGE_PLANT_CARD_QUERY = """
                SELECT filename, storage_path, content_type
                FROM images
                WHERE plant_id = :id
                LIMIT 1
            """;

    public static final String FETCH_IMAGE_PLANT_CARDS = "Image.fetchImageCards";
    public static final String FETCH_IMAGE_PLANT_CARDS_QUERY = """
                SELECT filename, storage_path
                FROM images
                WHERE plant_id = :id
            """;

    public static final String FETCH_PLANT_TAXONOMY = "Plant.fetchPlantTaxonomy";
    public static final String FETCH_PLANT_TAXONOMY_QUERY = """
                SELECT p.id, p.scientific_name, p.price, p.updated_at,
                       t.division, t.class, t.taxonomic_order, t.family, t.genus, t.species
                FROM plants p
                LEFT JOIN taxonomies t
                    ON t.plant_id = p.id
                WHERE p.id = :id
            """;

    public static final String FETCH_COMMON_NAME_TO_PLANT_DETAILS = "CommonName.fetchCommonNameToPlantDetails";
    public static final String FETCH_COMMON_NAME_TO_PLANT_DETAILS_QUERY = """
                SELECT cn.id, cn.name, cn.country, cn.place
                FROM common_names cn
                WHERE cn.plant_id = :id
            """;
}
