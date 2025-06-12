package com.example.ADPS.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.content.jpa.config.EnableJpaStores;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
@EnableFilesystemStores
@EnableJpaStores
public class StorageConfig {

    @Value("${document.storage.root:./documents}")
    private String storageRoot;

    @Bean
    public File filesystemRoot() throws IOException {
        File root = Paths.get(storageRoot).toFile();
        if (!root.exists()) {
            Files.createDirectories(root.toPath());
        }
        return root;
    }

    @Bean
    public FileSystemResourceLoader fileSystemResourceLoader() throws IOException {
        return new FileSystemResourceLoader(filesystemRoot().getAbsolutePath());
    }

    @Bean
    public String documentStoragePath() {
        return storageRoot;
    }
}