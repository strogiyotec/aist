package org.aist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.http.HttpClient;
import java.util.List;
import lombok.Data;
import org.aist.http.AppointmentsRequest;
import org.aist.http.AppointmentsRequestImpl;
import org.aist.http.LoginPage;
import org.aist.http.LoginPageImpl;
import org.aist.http.LoginRequest;
import org.aist.http.LoginRequestImpl;
import org.aist.http.headers.Headers;
import org.aist.telegram.TelegramBotCommandsImpl;

/**
 * Main.
 */
public class App {
    public static void main(String[] args) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        final CliArgs cliArgs = new CliArgs();
        final HttpClient httpClient = HttpClient.newBuilder().build();
        final EarlyDateLoop loop = new EarlyDateLoop(new TelegramBotCommandsImpl(httpClient, cliArgs.getBotToken(), cliArgs.getChatId()), httpClient, objectMapper);
        loop.run(cliArgs);
    }

    @Data
    static class CliArgs {
        private final String password;

        private final String email;

        private final String botToken;

        private final String chatId;

        public CliArgs() {
            this.password = System.getProperty("password");
            this.email = System.getProperty("email");
            this.botToken = System.getProperty("botToken");
            this.chatId = System.getProperty("chatId");
        }
    }
}
