package org.aist.http.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.function.Supplier;

public final class HealthServer {

    public static void main(String[] args) throws IOException {
        new HealthServer(() -> LocalDate.now(),8080);
    }

    public HealthServer(final Supplier<LocalDate> dateSupplier, final int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/health", this.healthHandler(dateSupplier));
        server.setExecutor(null);
    }

    private HttpHandler healthHandler(final Supplier<LocalDate> dateSupplier) {
        return exchange -> {
            final String response = String.format("Latest date is %s", dateSupplier.get().toString());
            exchange.sendResponseHeaders(200, response.length());
            final OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        };
    }
}
