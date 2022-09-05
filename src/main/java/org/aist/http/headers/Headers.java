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

    public static String[] appointmentsHeaders(final String yatriSession) {
        return new String[]{
            "User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:104.0) Gecko/20100101 Firefox/104.0",
            "Accept", "application/json, text/javascript, */*; q=0.01",
            "Accept-Language", "en-US,en;q=0.5",
            "Accept-Encoding", "gzip, deflate, br",
            "Referer", "https://ais.usvisa-info.com/en-ca/niv/schedule/43348961/appointment",
            "X-Requested-With", "XMLHttpRequest",
            "Sec-Fetch-Dest", "empty",
            "Sec-Fetch-Mode", "cors",
            "Sec-Fetch-Site", "same-origin",
            "Cookie", yatriSession
        };
    }

    public static String[] loginRequestHeaders(final String yatriSession) {
        return new String[]{
            "User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:104.0) Gecko/20100101 Firefox/104.0",
            "Host", "ais.usvisa-info.com",
            "Referer", "https://ais.usvisa-info.com/en-ca/niv/users/sign_in",
            "Content-Type", "application/x-www-form-urlencoded",
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language", "en-US,en;q=0.5",
            "Accept-Encoding", "gzip, deflate, br",
            "Origin", "https://ais.usvisa-info.com",
            "Upgrade-Insecure-Requests", "1",
            "Sec-Fetch-Dest", "document",
            "Sec-Fetch-Mode", "navigate",
            "Sec-Fetch-Site", "same-origin",
            "Sec-Fetch-User", "?1",
            "DNT", "1",
            "Cookie", yatriSession
        };
    }
}
