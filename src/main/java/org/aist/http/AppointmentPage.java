package org.aist.http;

import lombok.Data;

public interface AppointmentPage {

    AppointmentPage.Response get(AppointmentPage.Request request) throws Exception;

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
