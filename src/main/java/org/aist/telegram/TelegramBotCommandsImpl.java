package org.aist.telegram;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import lombok.AllArgsConstructor;
import org.aist.http.Preconditions;

@AllArgsConstructor
public final class TelegramBotCommandsImpl implements TelegramBotCommands {

    private final HttpClient httpClient;

    private final String botApiKey;

    private final String chatId;

    @Override
    public void publishEarliestDate(final LocalDate localDate) throws Exception {
        final HttpRequest request = HttpRequest.newBuilder(
                new URI(
                    String.format(
                        "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                        this.botApiKey,
                        this.chatId,
                        TelegramBotCommandsImpl.formatMessage(localDate)
                    )
                )
            )
            .GET()
            .build();
        final HttpResponse<String> send = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Preconditions.checkStatusCode(send.statusCode(), 200);
    }

    @Override
    public String getLastMessage() {
        throw new UnsupportedOperationException("#getLastMessage()");
    }

    private static String formatMessage(final ChronoLocalDate localDate) {
        return String.format("Earliest date is [%s]", localDate.toString());
    }
}
