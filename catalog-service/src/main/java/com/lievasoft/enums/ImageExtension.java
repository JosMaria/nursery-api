package com.lievasoft.enums;

public enum ImageExtension {
    WEBP("webp"),
    GIF("gif"),
    PNG("png"),
    JPEG("jpeg"),
    JPG("jpg");

    private final String extension;

    ImageExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }
}
