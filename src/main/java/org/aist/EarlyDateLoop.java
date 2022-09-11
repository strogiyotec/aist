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
                    Headers.appointmentsHeaders(
                        credentials.getYatri(),
                        credentials.getCsrf(),
                        credentials.getAccountNumber()
                    )
                )
            );
            this.updateEarliestAppointment(latestAppointments);
            TimeUnit.SECONDS.sleep(30L);
            iterations++;
            if (iterations == EarlyDateLoop.GET_APPOINTMENTS_RESET_CNT) {
                iterations = 0;
                System.out.println("Reset credentials");
                credentials = this.getCredentials(args);
                System.out.println("update session token");
            }
        }
    }

    private void updateEarliestAppointment(final List<AppointmentsApi.ResponsePayload> latestAppointments) throws Exception {
        if (latestAppointments.isEmpty()) {
            System.out.println("No appointments");
        } else {
            System.out.println("Response is: " + latestAppointments);
            final LocalDate apiDate = latestAppointments.get(0).getDate();
            if (apiDate.isAfter(LocalDate.of(2023, Month.JANUARY, 1))) {
                System.out.println("The date is only for next year");
            } else {
                System.out.println("earliest appointment is " + apiDate);
                this.commands.publishEarliestDate(apiDate);
            }
        }
    }

    private AppointmentsApi.Credentials getCredentials(App.CliArgs cliArgs) throws Exception {
        final LoginHtmlPage.ResponsePayload loginPage = this.loginPage.get(Headers.loginPageHeaders());
        final SignInApi.Response loginApiResponse = this.loginRequest.signIn(
            new SignInApi.Request(
                cliArgs.getEmail(),
                cliArgs.getPassword(),
                Headers.loginRequestHeaders(
                    loginPage.getYatriSession()
                ),
                loginPage.getCsrfToken()
            )
        );
        final AppointmentHtmlPage.Response appointmentPage = this.appointmentPage.get(
            new AppointmentHtmlPage.Request(loginApiResponse.getLocation(), loginApiResponse.getYatriSession())
        );
        return new AppointmentsApi.Credentials(
            appointmentPage.getCsrf(),
            loginApiResponse.getYatriSession(),
            appointmentPage.getAccountNumber()
        );
    }
}
