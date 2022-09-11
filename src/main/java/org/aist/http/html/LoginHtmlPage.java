package org.aist.http.html;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface LoginHtmlPage {

    /**
     * The csrf token from login page.
     * We need it to send an actual sign in request
     */
    LoginHtmlPage.ResponsePayload get(String... headers) throws Exception;

    @AllArgsConstructor
    @Data
    class ResponsePayload {
        private final String csrfToken;

        private final String yatriSession;
    }
}
