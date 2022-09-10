package org.aist.http.headers;

public final class Headers {

    public static String[] loginPageHeaders() {
        return new String[]{
            "User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:104.0) Gecko/20100101 Firefox/104.0",
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language", "en-US,en;q=0.5",
            "Accept-Encoding", "gzip, deflate, br"
        };
    }

    public static String[] appointmentsHeaders(final String yatriSession, final String csrf, final String referer) {
        return new String[]{
            "Accept", "application/json, text/javascript, */*; q=0.01",
            "Accept-Language", "en-US,en;q=0.5",
            "Accept-Encoding", "gzip, deflate, br",
            "Cookie", yatriSession,
            "DNT", "1",
            "Host", "ais.usvisa-info.com",
            "Referer", String.format("https://ais.usvisa-info.com/en-ca/niv/schedule/%s/continue_actions", referer),
            "Sec-Fetch-Dest", "empty",
            "Sec-Fetch-Mode", "cors",
            "Sec-Fetch-Site", "same-origin",
            "User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:104.0) Gecko/20100101 Firefox/104.0",
            "X-Requested-With", "XMLHttpRequest",
            "X-CSRF-Token", csrf

        };
    }

    public static String[] loginRequestHeaders(final String yatriSession) {
        return new String[]{
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language", "en-US,en;q=0.5",
            "Accept-Encoding", "gzip, deflate, br",
            "Content-Type", "application/x-www-form-urlencoded",
            "Cookie", yatriSession,
            "DNT", "1",
            "Host", "ais.usvisa-info.com",
            "Origin", "https://ais.usvisa-info.com",
            "Referer", "https://ais.usvisa-info.com/en-ca/niv/users/sign_in",
            "Sec-Fetch-Dest", "document",
            "Sec-Fetch-Mode", "navigate",
            "Sec-Fetch-Site", "same-origin",
            "Sec-Fetch-User", "?1",
            "User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:104.0) Gecko/20100101 Firefox/104.0",
            "Upgrade-Insecure-Requests", "1",
        };
    }

    public static String[] headersWithCookieAndReferer(final String yatri, final String referer) {
        return new String[]{
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language", "en-US,en;q=0.5",
            "Accept-Encoding", "gzip, deflate, br",
            "Content-Type", "application/x-www-form-urlencoded",
            "Cookie", yatri,
            "DNT", "1",
            "Host", "ais.usvisa-info.com",
            "Origin", "https://ais.usvisa-info.com",
            "Referer", referer,
            "Sec-Fetch-Dest", "document",
            "Sec-Fetch-Mode", "navigate",
            "Sec-Fetch-Site", "same-origin",
            "Sec-Fetch-User", "?1",
            "User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:104.0) Gecko/20100101 Firefox/104.0",
            "Upgrade-Insecure-Requests", "1",
        };
    }
}
