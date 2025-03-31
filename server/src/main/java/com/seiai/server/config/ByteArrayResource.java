package com.seiai.server.config;

import org.springframework.core.io.AbstractResource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayResource extends AbstractResource {
    private final byte[] byteArray;

    public ByteArrayResource(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    @Override
    public String getDescription() {
        return "Byte array resource";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(byteArray);
    }
}