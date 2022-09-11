package org.aist.http.html;

import lombok.Data;

public interface AppointmentHtmlPage {

    AppointmentHtmlPage.Response get(AppointmentHtmlPage.Request request) throws Exception;

    @Data
    class Response {

        private final String accountNumber;

        private final String csrf;
    }

    @Data
    class Request {
        private final String url;

        private final String yatri;
    }
}
