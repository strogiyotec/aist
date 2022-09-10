package org.aist.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.aist.http.cookies.Cookies;
import org.aist.http.post.PostData;

@AllArgsConstructor
public final class SignInRequestImpl implements SignInRequest {

    private final HttpClient httpClient;

    @Override
    public SignInRequest.Response send(Request payload) throws Exception {
        final HttpRequest request = SignInRequestImpl.request(payload);
        final HttpResponse<Void> response = this.httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        Preconditions.checkStatusCode(response.statusCode(), 302);
        return new SignInRequest.Response(
            Cookies.fetchYatri(response.headers()),
            response.headers().firstValue("Location").orElseThrow(() -> new IllegalStateException("Sign in response doesn't have redirect page"))
        );
    }

    private static HttpRequest request(final Request payload) {
        return HttpRequest.newBuilder(URI.create("https://ais.usvisa-info.com/en-ca/niv/users/sign_in"))
            .headers(payload.getHeaders())
            .POST(HttpRequest.BodyPublishers.ofString(
                PostData.ofMap(
                    SignInRequestImpl.formData(payload)
                )
            ))
            .build();
    }

    private static Map<String, String> formData(final Request payload) {
        return Map.of(
            "utf8", "✓",
            "authenticity_token", payload.getCsrfToken(),
            "user[email]", payload.getUsername(),
            "user[password]", payload.getPassword(),
            "policy_confirmed", "1",
            "commit", "Sign+In"
        );
    }
}
