package org.aist.http.cookies;

import java.net.http.HttpHeaders;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Cookies {

    private static final Pattern YARNI_TOKEN_PTRN = Pattern.compile("(_yatri_session=[^;]+).*");

    public static String fetchYatri(final HttpHeaders headers) {
        final String cookie = headers.firstValue("Set-Cookie").orElseThrow(() -> new IllegalStateException("No cookies from response for sign in page"));
        final Matcher matcher = Cookies.YARNI_TOKEN_PTRN.matcher(cookie);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            throw new IllegalStateException("Can't find yatri session in the sign-in page ");
        }
    }
}
