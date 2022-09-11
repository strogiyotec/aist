package org.aist.http.api;

import lombok.Data;

public interface SignInApi {

    /**
     * Perform login and return cookies with auth.
     */
    SignInApi.Response signIn(SignInApi.Request payload) throws Exception;

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
