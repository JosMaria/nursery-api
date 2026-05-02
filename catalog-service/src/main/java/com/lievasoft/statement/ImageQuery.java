package com.lievasoft.statement;

public class ImageQuery {

    public static final String IMAGE_SELECTION_PER_PLANT = "Image.fetchImagesSelectionPerPlant";

    public static final String IMAGE_SELECTION_PER_PLANT_QUERY = """ 
                SELECT id, is_selected
                FROM images
                WHERE plant_id = :plantId
                ORDER BY is_selected DESC
            """;

    public static final String FETCH_IMAGE_PLANT_CARDS = "Image.fetchImageCards";
    public static final String FETCH_IMAGE_PLANT_CARDS_QUERY = """
                SELECT filename, storage_path
                FROM images
                WHERE plant_id = :id
            """;
}
