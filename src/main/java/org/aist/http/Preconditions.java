package org.aist.http;

public final class Preconditions {
    public static void checkStatusCode(final int statusCode, final int expected) {
        if (statusCode != expected) {
            throw new IllegalStateException(String.format("Status code for sign in page is not %d", expected));
        }
    }
}
