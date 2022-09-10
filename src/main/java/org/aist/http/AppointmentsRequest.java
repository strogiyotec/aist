package org.aist.http;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

public interface AppointmentsRequest {

    List<AppointmentsRequest.ResponsePayload> getLatestAppointments(AppointmentsRequest.RequestPayload requestPayload) throws Exception;

    @AllArgsConstructor
    @Data
    class RequestPayload {

        private final String accountId;

        private final String[] headers;
    }

    @Data
    class Credentials{

        private final String csrf;

        private final String yatri;

        private final String accountNumber;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ResponsePayload {

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;

    }
}
