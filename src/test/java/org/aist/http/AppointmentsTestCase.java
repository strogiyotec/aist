package org.aist.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import org.aist.http.headers.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

public final class AppointmentsTestCase {

    @Test
    void test() throws Exception {
        //given
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var client = AppointmentsTestCase.client();
        var appointmentsRequest = new AppointmentsRequestImpl(
            client, objectMapper
        );
        //when
        var response = appointmentsRequest.getLatestAppointments(
            new AppointmentsRequest.RequestPayload(
                "test",
                Headers.appointmentsHeaders("test","test","test")
            )
        );
        Assertions.assertFalse(response.isEmpty());
    }

    private static HttpClient client() throws IOException, InterruptedException {
        final HttpClient mock = Mockito.mock(HttpClient.class);
        final HttpResponse response = Mockito.mock(HttpResponse.class);
        Mockito.when(response.statusCode()).thenReturn(200);
        Mockito.when(response.body()).thenReturn("[{\"date\":\"2023-11-29\",\"business_day\":true},{\"date\":\"2023-11-30\",\"business_day\":true}]");
        Mockito.when(mock.send(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(response);
        return mock;
    }
}
