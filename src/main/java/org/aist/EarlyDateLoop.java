package org.aist;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.aist.http.AppointmentsRequest;
import org.aist.http.AppointmentsRequestImpl;
import org.aist.http.LoginPage;
import org.aist.http.LoginPageImpl;
import org.aist.http.LoginRequest;
import org.aist.http.LoginRequestImpl;
import org.aist.http.headers.Headers;
import org.aist.telegram.TelegramBotCommands;

public final class EarlyDateLoop {

    /**
     * Try getting appointments this amount of times with same token then use new token.
     */
    private static final int GET_APPOINTMENTS_RESET_CNT = 40;

    private final TelegramBotCommands commands;

    private final HttpClient httpClient;

    private final AppointmentsRequest appointmentsRequest;

    private final LoginPage loginPage;

    private final LoginRequest loginRequest;

    private LocalDate lastDate;

    public EarlyDateLoop(final TelegramBotCommands commands, final HttpClient httpClient, final ObjectMapper objectMapper) {
        this.commands = commands;
        this.httpClient = httpClient;
        this.appointmentsRequest = new AppointmentsRequestImpl(httpClient, objectMapper);
        this.loginPage = new LoginPageImpl(httpClient);
        this.loginRequest = new LoginRequestImpl(httpClient);
    }

    public void run(final App.CliArgs args) throws Exception {
        String sessionToken = this.getSessionToken(args);
        System.out.println("Got session token");
        int iterations = 0;
        while (true) {
            System.out.println("Send get appointments request");
            var latestAppointments = this.appointmentsRequest.getLatestAppointments(
                new AppointmentsRequest.RequestPayload(
                    Headers.appointmentsHeaders(sessionToken)
                )
            );
            this.updateEarliestAppointment(latestAppointments);
            TimeUnit.SECONDS.sleep(30L);
            iterations++;
            if (iterations == EarlyDateLoop.GET_APPOINTMENTS_RESET_CNT) {
                iterations = 0;
                if (this.lastDate == null) {
                    this.commands.publishNoAppointments(Duration.of(60L/*20 iterations * 30 seconds sleep*/, ChronoUnit.MINUTES));
                } else {
                    this.commands.publishCurrentDate(this.lastDate, Duration.of(60L, ChronoUnit.MINUTES));
                }
                sessionToken = this.getSessionToken(args);
                System.out.println("update session token");
            }
        }
    }

    private void updateEarliestAppointment(final List<AppointmentsRequest.ResponsePayload> latestAppointments) throws Exception {
        if (latestAppointments.isEmpty()) {
            System.out.println("No appointments for next 90 days");
            this.lastDate = null;
        } else {
            System.out.println("Response is: " + latestAppointments);
            final LocalDate apiDate = latestAppointments.get(0).getDate();
            if(apiDate.isBefore(this.lastDate)){
                this.lastDate = apiDate;
                System.out.println("earliest appointment is " + this.lastDate);
                this.commands.publishEarliestDate(latestAppointments.get(0).getDate());
            }
        }
    }

    private String getSessionToken(App.CliArgs cliArgs) throws Exception {
        final LoginPage.ResponsePayload loginPagePayload = this.loginPage.get(Headers.loginPageHeaders());
        return this.loginRequest.send(
            new LoginRequest.Payload(
                cliArgs.getEmail(),
                cliArgs.getPassword(),
                Headers.loginRequestHeaders(
                    loginPagePayload.getYatriSession()
                ),
                loginPagePayload.getCsrfToken()
            )
        );
    }
}
