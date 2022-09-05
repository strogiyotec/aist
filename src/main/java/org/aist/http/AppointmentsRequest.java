package org.aist.http;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

public interface AppointmentsRequest {

    List<ResponsePayload> getLatestAppointments(RequestPayload requestPayload) throws Exception;

    @AllArgsConstructor
    @Data
    class RequestPayload {

        private final String[] headers;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    class ResponsePayload {

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;

    }
}
