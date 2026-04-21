package com.lievasoft.statement;

public class ImageQuery {

    public static final String IMAGE_SELECTION_PER_PLANT = "Image.fetchImagesSelectionPerPlant";

    public static final String IMAGE_SELECTION_PER_PLANT_QUERY = """ 
            SELECT id, is_selected, plant_id
            FROM images
            WHERE plant_id = :plantId
            """;
}
