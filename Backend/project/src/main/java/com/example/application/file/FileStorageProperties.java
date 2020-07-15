package com.example.application.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * This class sets the file storage properities for upload 
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}