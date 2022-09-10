package org.aist.http;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface SignInRequest {

    /**
     * Perform login and return cookies with auth.
     */
    SignInRequest.Response send(SignInRequest.Request payload) throws Exception;

    @Data
    class Request {
        private final String username;

        private final String password;

        private final String[] headers;

        private final String csrfToken;
    }

    @Data
    class Response {
        private final String yatriSession;

        private final String location;
    }
}
