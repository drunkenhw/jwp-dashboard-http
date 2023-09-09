package org.apache.coyote.http11;

import org.apache.catalina.servlet.exception.UncheckedServletException;
import org.apache.coyote.Adapter;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;
    private final Adapter adapter;

    public Http11Processor(final Socket connection, Adapter adapter) {
        this.connection = connection;
        this.adapter = adapter;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {
            var httpRequest = HttpRequest.parse(inputStream);
            var httpResponse = HttpResponse.create();
            adapter.service(httpRequest, httpResponse);
            outputStream.write(httpResponse.parse().getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }
}
