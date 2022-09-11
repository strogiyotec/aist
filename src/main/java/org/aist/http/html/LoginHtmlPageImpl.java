package org.aist.http.html;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.AllArgsConstructor;
import org.aist.http.csrf.Csrf;
import org.aist.http.Preconditions;
import org.aist.http.cookies.Cookies;

@AllArgsConstructor
public final class LoginHtmlPageImpl implements LoginHtmlPage {

    private static final String LOGIN_URL = "https://ais.usvisa-info.com/en-ca/niv/users/sign_in";

    private final HttpClient httpClient;

    @Override
    public LoginHtmlPage.ResponsePayload get(final String... headers) throws Exception {
        final HttpRequest request = LoginHtmlPageImpl.request(headers);
        final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Preconditions.checkStatusCode(response.statusCode(), 200);
        return new LoginHtmlPage.ResponsePayload(
            Csrf.fetchToken(response.body()),
            Cookies.fetchYatri(response.headers())
        );
    }

    private static HttpRequest request(final String[] headers) throws URISyntaxException {
        return HttpRequest.newBuilder(new URI(LoginHtmlPageImpl.LOGIN_URL))
            .headers(headers)
            .GET()
            .build();
    }
}
