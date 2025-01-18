package ru.yandex.practicum.blog.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.blog.configuration.RootAppConfiguration;
import ru.yandex.practicum.blog.exception.StorageException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileSystemStorageUtil implements StorageUtil {
    private final Path rootPath;

    public FileSystemStorageUtil(RootAppConfiguration rootAppConfiguration) {
        this.rootPath = Paths.get(rootAppConfiguration.getStorageRootPath());
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("failed to store empty file");
            }

            Path destinationFile = rootPath.resolve(
                            Paths.get(generateRandomFileName(file.getOriginalFilename())))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return destinationFile.getFileName().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateRandomFileName(String originalName) {
        return UUID.randomUUID() + "." + FilenameUtils.getExtension(originalName);
    }
}
