package com.vidalink.healthcare.shared.application.port.out;

import java.io.InputStream;

public interface FileStorage {

    String upload(InputStream inputStream, String path, String contentType);

    InputStream download(String path);

    void delete(String path);
}
