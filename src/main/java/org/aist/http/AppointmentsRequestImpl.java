package org.aist.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class AppointmentsRequestImpl implements AppointmentsRequest {

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    @Override
    public List<ResponsePayload> getLatestAppointments(final RequestPayload requestPayload) throws Exception {
        final HttpRequest request = HttpRequest.newBuilder(new URI("https://ais.usvisa-info.com/en-ca/niv/schedule/43348961/appointment/days/95.json?appointments[expedite]=false"))
            .GET()
            .headers(requestPayload.getHeaders())
            .build();
        final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final String body = response.body();
        return this.objectMapper.readValue(
            body,
            new TypeReference<>() {
            }
        );
    }
}
