package org.aist.telegram;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import lombok.AllArgsConstructor;
import org.aist.http.Preconditions;

@AllArgsConstructor
public final class TelegramBotCommandsImpl implements TelegramBotCommands {

    private final HttpClient httpClient;

    private final String botApiKey;

    private final String chatId;

    @Override
    public void publishEarliestDate(final LocalDate localDate) throws Exception {
        final String message = String.format("Earliest date is [%s]", localDate.toString());
        final HttpRequest request = HttpRequest.newBuilder(new URI(this.sendMessageUrl(message)))
            .GET()
            .build();
        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Preconditions.checkStatusCode(send.statusCode(), 200);
    }

    @Override
    public void publishCurrentDate(final LocalDate localDate, final Duration duration) throws Exception {
        final String message = String.format(
            "I tried to get latest appointment slots for %d minutes and it's still the same [%s]",
            duration.get(ChronoUnit.MINUTES),
            localDate.toString()
        );
        final HttpRequest request = HttpRequest.newBuilder(new URI(this.sendMessageUrl(message)))
            .GET()
            .build();
        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Preconditions.checkStatusCode(send.statusCode(), 200);
    }

    @Override
    public void publishNoAppointments(final Duration duration) throws Exception {
        final String message = String.format(
            "I tried to get latest appointments for %d minutes but there are no appointment slots in the next 90 days",
            duration.get(ChronoUnit.MINUTES)
        );
        final HttpRequest request = HttpRequest.newBuilder(new URI(this.sendMessageUrl(message)))
            .GET()
            .build();
        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Preconditions.checkStatusCode(send.statusCode(), 200);
    }

    private String sendMessageUrl(final String message) {
        return String.format(
            "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
            this.botApiKey,
            this.chatId,
            message
        );
    }
}
