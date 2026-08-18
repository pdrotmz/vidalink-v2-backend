package com.vidalink.healthcare.marketplace.infrastructure.persistence.storage.minio;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MinioFileStorageTest {

    @Mock
    private MinioClient minioClient;

    private MinioFileStorage fileStorage;

    @BeforeEach
    void setUp() {
        fileStorage = new MinioFileStorage(minioClient);
    }

    @Test
    void shouldUploadFile() throws Exception {
        String path = "rewards/text-image.png";
        byte[] content = "Vidalink MinIO Test".getBytes();

        String result = fileStorage.upload(
                new ByteArrayInputStream(content),
                path,
                "text/plain"
        );

        assertThat(result).isEqualTo(path);

        verify(minioClient).putObject(any(PutObjectArgs.class));
    }
}