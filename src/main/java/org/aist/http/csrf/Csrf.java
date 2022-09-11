package org.aist.http.csrf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Csrf {

    private static final Pattern CSRF_PTRN = Pattern.compile("name=\"csrf-token\" content=\"([^\"]+)");

    public static String fetchToken(final String html) {
        final Matcher matcher = Csrf.CSRF_PTRN.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalStateException("No csrf token in html page");
        }
    }
}
