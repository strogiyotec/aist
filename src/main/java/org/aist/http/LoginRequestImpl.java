package org.aist.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.aist.http.cookies.Cookies;
import org.aist.http.post.PostData;

@AllArgsConstructor
public final class LoginRequestImpl implements LoginRequest {

    private final HttpClient httpClient;

    @Override
    public String send(LoginRequest.Payload payload) throws Exception {
        final HttpRequest request = LoginRequestImpl.request(payload);
        final HttpResponse<Void> response = this.httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        Preconditions.checkStatusCode(response.statusCode(), 302);
        return Cookies.fetchYatri(response.headers());
    }

    private static HttpRequest request(final Payload payload) {
        return HttpRequest.newBuilder(URI.create("https://ais.usvisa-info.com/en-ca/niv/users/sign_in"))
            .headers(payload.getHeaders())
            .POST(HttpRequest.BodyPublishers.ofString(
                PostData.ofMap(
                    LoginRequestImpl.formData(payload)
                )
            ))
            .build();
    }

    private static Map<String, String> formData(final Payload payload) {
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
