package com.lievasoft.entity;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "images")
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
}
