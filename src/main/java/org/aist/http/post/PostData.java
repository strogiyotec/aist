package org.aist.http.post;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class PostData {
    public static String ofMap(final Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            if ("commit".equals(singleEntry.getKey())) {
                formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
                formBodyBuilder.append("=");
                formBodyBuilder.append(singleEntry.getValue());
            } else {
                formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
                formBodyBuilder.append("=");
                formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
            }
        }
        return formBodyBuilder.toString();
    }
}
