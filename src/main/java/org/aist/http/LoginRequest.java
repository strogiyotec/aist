package org.aist.http;

import java.io.IOException;
import java.net.URISyntaxException;
import lombok.AllArgsConstructor;
import lombok.Data;

public interface LoginRequest {

    /**
     * Perform login and return cookies with auth.
     */
    String send(LoginRequest.Payload payload) throws Exception;

    @AllArgsConstructor
    @Data
    class Payload {
        private final String username;

        private final String password;

        private final String[] headers;

        private final String csrfToken;
    }
}
