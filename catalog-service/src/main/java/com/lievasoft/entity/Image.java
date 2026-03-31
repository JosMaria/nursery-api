package com.lievasoft.entity;

import com.lievasoft.dto.response.ImageCardResponse;
import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "images")
@NamedNativeQuery(
        name = "Image.fetchImageCard",
        query = """
            SELECT url, content_type
            FROM images
            WHERE plant_id = :id
            LIMIT 1;
        """,
        resultSetMapping = "ImageCardMapping"
)
@SqlResultSetMapping(
        name = "ImageCardMapping",
        classes = @ConstructorResult(
                targetClass = ImageCardResponse.class,
                columns = {
                        @ColumnResult(name = "url", type = String.class),
                        @ColumnResult(name = "content_type", type = String.class)
                }
        )
)
public class Image {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "image_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type", length = 20)
    private String contentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    public Image() {
    }

    public Image(String url, String fileName, long fileSize, String contentType) {
        this.url = url;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
