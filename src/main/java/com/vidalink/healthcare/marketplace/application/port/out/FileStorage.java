package com.vidalink.healthcare.marketplace.application.port.out;

import java.io.InputStream;

public interface FileStorage {

    String upload(InputStream inputStream, String path, String contentType);

    void delete(String path);
}
