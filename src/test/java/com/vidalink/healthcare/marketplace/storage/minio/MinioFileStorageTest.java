package com.vidalink.healthcare.marketplace.storage.minio;

import com.vidalink.healthcare.marketplace.application.port.out.FileStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MinioFileStorageTest {

    @Autowired
    private FileStorage fileStorage;

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
    }
}
