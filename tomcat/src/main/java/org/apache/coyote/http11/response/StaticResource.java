package org.apache.coyote.http11.response;

import org.apache.catalina.servlet.exception.NotFoundFileException;

import java.io.IOException;
import java.io.InputStream;

public class StaticResource {

    private static final String FILE_PATH = "static";
    private static final String FILE_DELIMITER = "\\.";

    private final byte[] bytes;
    private final String fileExtension;

    private StaticResource(byte[] bytes, String fileExtension) {
        this.bytes = bytes;
        this.fileExtension = fileExtension;
    }

    public static StaticResource of(String uri) {
        try {
            InputStream resourceAsStream = ClassLoader.getSystemResourceAsStream(FILE_PATH + uri);
            byte[] bytes = resourceAsStream.readAllBytes();
            return new StaticResource(bytes, extractFileExtension(uri));
        } catch (IOException | NullPointerException e) {
            throw new NotFoundFileException();
        }
    }

    private static String extractFileExtension(String uri) {
        return uri.split(FILE_DELIMITER)[1].toLowerCase();
    }

    public String fileToString() {
        return new String(bytes);
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
