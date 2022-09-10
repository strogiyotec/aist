package org.aist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.http.HttpClient;
import lombok.Data;
import org.aist.telegram.TelegramBotCommandsImpl;

/**
 * Main.
 */
public class App {
    public static void main(String[] args) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        final App.CliArgs cliArgs = new App.CliArgs();
        final HttpClient httpClient = HttpClient.newBuilder().build();
        final EarlyDateLoop loop = new EarlyDateLoop(
            new TelegramBotCommandsImpl(
                httpClient,
                cliArgs.getBotToken(),
                cliArgs.getChatId(),
                cliArgs.getLabel()
            ),
            httpClient,
            objectMapper
        );
        loop.run(cliArgs);
    }

    @Data
    static class CliArgs {
        private final String password;

        private final String email;

        private final String botToken;

        private final String chatId;

        private final String label;

        public CliArgs() {
            this.password = System.getProperty("password");
            this.email = System.getProperty("email");
            this.botToken = System.getProperty("botToken");
            this.chatId = System.getProperty("chatId");
            this.label = System.getProperty("label");
        }
    }
}
