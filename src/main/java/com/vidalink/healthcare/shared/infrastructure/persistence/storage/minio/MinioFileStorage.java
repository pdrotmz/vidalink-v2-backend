package com.vidalink.healthcare.shared.infrastructure.persistence.storage.minio;

import com.vidalink.healthcare.shared.application.port.out.FileStorage;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public InputStream download(String path) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket("vidalink")
                            .object(path)
                            .build()
            );
        } catch (Exception exception) {
            throw new RuntimeException("Error downloading file from MinIO", exception);
        }
    }

    @Override
    public void delete(String path) {

    }
}
