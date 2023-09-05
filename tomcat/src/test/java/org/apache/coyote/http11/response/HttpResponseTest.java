package org.apache.coyote.http11.response;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HttpResponseTest {

    @Test
    void 상태코드와_응답바디로_응답을_생성한다() {
        // given
        HttpResponse httpResponse = HttpResponse.create();
        ResponseBody responseBody = ResponseBody.of("Hello, World!", "html");
        httpResponse.setResponseBody(responseBody);

        // when
        String response = httpResponse.parse();

        // then
        assertThat(response).isEqualTo("HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 13 \r\n" +
                "\r\n" +
                "Hello, World!");
    }

    @Test
    void 리다이렉트_응답을_생성한다() {
        // given
        HttpResponse httpResponse = HttpResponse.create();
        httpResponse.setStatusCode(HttpStatusCode.FOUND);
        httpResponse.location("/index.html");

        // when
        String response = httpResponse.parse();

        // then
        assertThat(response).isEqualTo("HTTP/1.1 302 Found \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 0 \r\n" +
                "Location: /index.html \r\n" +
                "\r\n");
    }
}
