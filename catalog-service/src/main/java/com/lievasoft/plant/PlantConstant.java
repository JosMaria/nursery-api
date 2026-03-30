package com.lievasoft.plant;

public class PlantConstant {

    public static final String FETCH_PLANT_CARDS_NAME = "Plant.fetchPlantCards";
    public static final String FETCH_PLANT_CARDS_QUERY = """
                    SELECT p.id, p.scientific_name, cn.name, p.price, i.url
                    FROM plants p
                    LEFT JOIN (
                        SELECT DISTINCT ON (plant_id) plant_id, url
                        FROM images
                    ) AS i ON i.plant_id = p.id
                    LEFT JOIN (
                        SELECT plant_id, name
                        FROM common_names
                        WHERE is_selected = TRUE
                    ) AS cn ON cn.plant_id = p.id;
            """;

    public static final String FETCH_PLANT_DETAILS_NAME = "Plant.fetchPlantDetailsById";
    public static final String FETCH_PLANT_DETAILS_QUERY = """
                    SELECT id, common_name, scientific_name, is_available, updated_at
                    FROM plants
                    WHERE id = :id
            """;
}
