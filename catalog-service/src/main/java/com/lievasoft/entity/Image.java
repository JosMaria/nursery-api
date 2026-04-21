package com.lievasoft.entity;

import com.lievasoft.dto.plant.ImageToPlantDetailsDTO;
import com.lievasoft.dto.response.ImageCardResponse;
import com.lievasoft.dto.response.image.ImageSelectionResponse;
import jakarta.persistence.*;

import static com.lievasoft.plant.PlantConstant.*;
import static com.lievasoft.statement.ImageQuery.IMAGE_SELECTION_PER_PLANT;
import static com.lievasoft.statement.ImageQuery.IMAGE_SELECTION_PER_PLANT_QUERY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "images")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = IMAGE_SELECTION_PER_PLANT,
                query = IMAGE_SELECTION_PER_PLANT_QUERY,
                resultSetMapping = "ImageSelectionResponse"
        ),
        @NamedNativeQuery(
                name = FETCH_IMAGE_PLANT_CARD,
                query = FETCH_IMAGE_PLANT_CARD_QUERY,
                resultSetMapping = "ImageCardMapping"
        ),
        @NamedNativeQuery(
                name = FETCH_IMAGE_PLANT_CARDS,
                query = FETCH_IMAGE_PLANT_CARDS_QUERY,
                resultSetMapping = "ImageCardToPlantDetails"
        ),
})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "ImageSelectionResponse",
                classes = @ConstructorResult(
                        targetClass = ImageSelectionResponse.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "filename", type = String.class),
                                @ColumnResult(name = "is_selected", type = Boolean.class),
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "ImageCardMapping",
                classes = @ConstructorResult(
                        targetClass = ImageCardResponse.class,
                        columns = {
                                @ColumnResult(name = "filename", type = String.class),
                                @ColumnResult(name = "storage_path", type = String.class),
                                @ColumnResult(name = "content_type", type = String.class)
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "ImageCardToPlantDetails",
                classes = @ConstructorResult(
                        targetClass = ImageToPlantDetailsDTO.class,
                        columns = {
                                @ColumnResult(name = "filename", type = String.class),
                                @ColumnResult(name = "storage_path", type = String.class)
                        }
                )
        )
})
public class Image {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "image_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "storage_path", nullable = false)
    private String storagePath;

    private String filename;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type", length = 20)
    private String contentType;

    @Column(name = "is_selected")
    private Boolean isSelected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    public Image() {
    }

    public Image(String storagePath, String filename, long fileSize, String contentType, boolean isSelected) {
        this.storagePath = storagePath;
        this.filename = filename;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.isSelected = isSelected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoragePath() {
        return this.storagePath;
    }

    public Boolean getSelected() {
        return this.isSelected;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
