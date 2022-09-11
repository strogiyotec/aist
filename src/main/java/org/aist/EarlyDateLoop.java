package org.aist;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.aist.http.html.AppointmentHtmlPage;
import org.aist.http.html.AppointmentHtmlPageImpl;
import org.aist.http.api.AppointmentsApi;
import org.aist.http.api.AppointmentsApiImpl;
import org.aist.http.html.LoginHtmlPage;
import org.aist.http.html.LoginHtmlPageImpl;
import org.aist.http.api.SignInApi;
import org.aist.http.api.SignInApiImpl;
import org.aist.http.headers.Headers;
import org.aist.telegram.TelegramBotCommands;

public final class EarlyDateLoop {

    /**
     * Try getting appointments this amount of times with same token then use new token.
     */
    private static final int GET_APPOINTMENTS_RESET_CNT = 40;

    private final TelegramBotCommands commands;

    private final AppointmentsApi appointmentsRequest;

    private final LoginHtmlPage loginPage;

    private final SignInApi loginRequest;

    private final AppointmentHtmlPage appointmentPage;

    private LocalDate lastDate;

    public EarlyDateLoop(final TelegramBotCommands commands, final HttpClient httpClient, final ObjectMapper objectMapper) {
        this.commands = commands;
        this.appointmentsRequest = new AppointmentsApiImpl(httpClient, objectMapper);
        this.loginPage = new LoginHtmlPageImpl(httpClient);
        this.loginRequest = new SignInApiImpl(httpClient);
        this.appointmentPage = new AppointmentHtmlPageImpl(httpClient);
    }

    public void run(final App.CliArgs args) throws Exception {
        AppointmentsApi.Credentials credentials = this.getCredentials(args);
        System.out.println("Got credentials");
        int iterations = 0;
        while (true) {
            System.out.println("Send get appointments request");
            var latestAppointments = this.appointmentsRequest.getLatestAppointments(
                new AppointmentsApi.RequestPayload(
                    credentials.getAccountNumber(),
                    Headers.appointmentsHeaders(credentials.getYatri(), credentials.getCsrf(), credentials.getAccountNumber())
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
                credentials = this.getCredentials(args);
                System.out.println("update session token");
            }
        }
    }

    private void updateEarliestAppointment(final List<AppointmentsApi.ResponsePayload> latestAppointments) throws Exception {
        if (latestAppointments.isEmpty()) {
            System.out.println("No appointments");
            this.lastDate = null;
        } else {
            System.out.println("Response is: " + latestAppointments);
            final LocalDate apiDate = latestAppointments.get(0).getDate();
            if (this.lastDate == null || apiDate.isBefore(this.lastDate)) {
                if (apiDate.isAfter(LocalDate.of(2023, Month.JANUARY, 1))) {
                    System.out.println("The date is only for next year");
                } else {
                    this.lastDate = apiDate;
                    System.out.println("earliest appointment is " + this.lastDate);
                    this.commands.publishEarliestDate(latestAppointments.get(0).getDate());
                }
            }
        }
    }

    private AppointmentsApi.Credentials getCredentials(App.CliArgs cliArgs) throws Exception {
        final LoginHtmlPage.ResponsePayload loginPagePayload = this.loginPage.get(Headers.loginPageHeaders());
        final SignInApi.Response loginPageResponse = this.loginRequest.signIn(
            new SignInApi.Request(
                cliArgs.getEmail(),
                cliArgs.getPassword(),
                Headers.loginRequestHeaders(
                    loginPagePayload.getYatriSession()
                ),
                loginPagePayload.getCsrfToken()
            )
        );
        final AppointmentHtmlPage.Response appointmentPageResponse = this.appointmentPage.get(
            new AppointmentHtmlPage.Request(loginPageResponse.getLocation(), loginPageResponse.getYatriSession())
        );
        return new AppointmentsApi.Credentials(
            appointmentPageResponse.getCsrf(),
            loginPageResponse.getYatriSession(),
            appointmentPageResponse.getAccountNumber()
        );
    }
}
