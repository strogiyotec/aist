package org.aist.http;

import java.net.http.HttpClient;
import org.aist.http.headers.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class LoginPageTestCase {

    @Test
    void testCsrfToken() throws Exception {
        //given
        var loginPage = new LoginPageImpl(HttpClient.newBuilder().build());
        //when
        var payload = loginPage.get(Headers.loginPageHeaders());
        //then
        Assertions.assertFalse(payload.getCsrfToken().isEmpty());
        Assertions.assertTrue(payload.getYatriSession().contains("yatri"));

    }
}
