package com.vidalink.healthcare.marketplace.infrastructure.persistence.storage.minio;

import com.vidalink.healthcare.marketplace.application.port.out.FileStorage;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class MinioFileStorage implements FileStorage {

    private final MinioClient minioClient;

    @Override
    public String upload(InputStream inputStream, String path, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket("vidalink")
                            .object(path)
                            .stream(inputStream, -1, 10 * 1024 * 1024)
                            .contentType(contentType)
                            .build()
            );

            return path;
        } catch (Exception exception){
            throw new RuntimeException("Error uploading file to minIO", exception);
        }
    }

    @Override
    public void delete(String path) {

    }
}
