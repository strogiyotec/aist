package org.aist.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.aist.http.Csrf.Csrf;
import org.aist.http.cookies.Cookies;

@AllArgsConstructor
public final class LoginPageImpl implements LoginPage {

    private static final String LOGIN_URL = "https://ais.usvisa-info.com/en-ca/niv/users/sign_in";

    private final HttpClient httpClient;

    @Override
    public LoginPage.ResponsePayload get(final String... headers) throws Exception {
        final HttpRequest request = LoginPageImpl.request(headers);
        final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Preconditions.checkStatusCode(response.statusCode(), 200);
        return new LoginPage.ResponsePayload(
            Csrf.fetchToken(response.body()),
            Cookies.fetchYatri(response.headers())
        );
    }

    private static HttpRequest request(final String[] headers) throws URISyntaxException {
        return HttpRequest.newBuilder(new URI(LoginPageImpl.LOGIN_URL))
            .headers(headers)
            .GET()
            .build();
    }
}
