package org.aist.http.html;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.AbstractMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.aist.http.csrf.Csrf;
import org.aist.http.Preconditions;
import org.aist.http.headers.Headers;

@AllArgsConstructor
public final class AppointmentHtmlPageImpl implements AppointmentHtmlPage {

    private static final Pattern APPOINTMENT_NUMBER_PTRN = Pattern.compile("en-ca/niv/schedule/([0-9]+).*");

    private final HttpClient httpClient;

    @Override
    public AppointmentHtmlPage.Response get(final AppointmentHtmlPage.Request request) throws Exception {
        //first we get group page from account page
        final HttpRequest groupRequest = this.groupPageRequest(request);
        final Map.Entry<String, HttpRequest> appointmentPageRequest = this.appointmentPage(request, groupRequest);
        final HttpResponse<String> response = this.httpClient.send(appointmentPageRequest.getValue(), HttpResponse.BodyHandlers.ofString());
        Preconditions.checkStatusCode(response.statusCode(), 200);
        return new AppointmentHtmlPage.Response(appointmentPageRequest.getKey(), Csrf.fetchToken(response.body()));
    }

    /**
     * Key is appointment number and value is a request.
     */
    private Map.Entry<String, HttpRequest> appointmentPage(final Request request, final HttpRequest groupRequest) throws Exception {
        final HttpResponse<String> groupResponse = this.httpClient.send(groupRequest, HttpResponse.BodyHandlers.ofString());
        Preconditions.checkStatusCode(groupResponse.statusCode(), 200);
        final String appointmentNumber = AppointmentHtmlPageImpl.appointmentNumber(groupResponse.body());
        final String referer = String.format("https://ais.usvisa-info.com/en-ca/niv/schedule/%s/continue_actions", appointmentNumber);
        final String url = String.format("https://ais.usvisa-info.com/en-ca/niv/schedule/%s/appointment", appointmentNumber);
        return new AbstractMap.SimpleEntry<>(
            appointmentNumber,
            HttpRequest.newBuilder(new URI(url))
                .GET()
                .headers(Headers.headersWithCookieAndReferer(request.getYatri(), referer))
                .build()
        );
    }

    private HttpRequest groupPageRequest(final Request request) throws URISyntaxException, IOException, InterruptedException {
        final HttpRequest accountPageReq = AppointmentHtmlPageImpl.accountPageRequest(request);
        final HttpResponse<Void> response = this.httpClient.send(accountPageReq, HttpResponse.BodyHandlers.discarding());
        Preconditions.checkStatusCode(response.statusCode(), 302);
        final String location = response.headers().firstValue("Location").orElseThrow(() -> new IllegalStateException("Account page does not redirect to group"));
        return HttpRequest.newBuilder(new URI(location))
            .GET()
            .headers(Headers.headersWithCookieAndReferer(request.getYatri(), "https://ais.usvisa-info.com/en-ca/niv/users/sign_in"))
            .build();
    }

    private static HttpRequest accountPageRequest(final AppointmentHtmlPage.Request request) throws URISyntaxException {
        return HttpRequest.newBuilder(new URI(request.getUrl()))
            .GET()
            .headers(Headers.headersWithCookieAndReferer(request.getYatri(), "https://ais.usvisa-info.com/en-ca/niv/users/sign_in"))
            .build();
    }

    private static String appointmentNumber(final String html) {
        final Matcher matcher = AppointmentHtmlPageImpl.APPOINTMENT_NUMBER_PTRN.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalStateException("No appointment number is found");
        }
    }
}
