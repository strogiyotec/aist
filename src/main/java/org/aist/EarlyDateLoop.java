package org.aist;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import org.aist.http.AppointmentsRequest;
import org.aist.http.headers.Headers;
import org.aist.telegram.TelegramBotCommands;

public final class EarlyDateLoop {

    private final TelegramBotCommands commands;

    private LocalDate lastDate;

    public EarlyDateLoop(final TelegramBotCommands commands) {
        this.commands = commands;
    }

    public void run(final String sessionToken, final AppointmentsRequest appointmentsRequest) throws Exception {
        while (true) {
            System.out.println("Send get appointments request");
            var latestAppointments = appointmentsRequest.getLatestAppointments(
                new AppointmentsRequest.RequestPayload(
                    Headers.appointmentsHeaders(sessionToken)
                )
            );
            if (latestAppointments.isEmpty()) {
                System.out.println("No appointments for next 90 days");
            } else {
                System.out.println("Response is: " + latestAppointments);
                if (this.lastDate == null || this.lastDate.isAfter(latestAppointments.get(0).getDate())) {
                    this.lastDate = latestAppointments.get(0).getDate();
                    this.commands.publishEarliestDate(this.lastDate);
                    System.out.println("New earliest appointment is " + this.lastDate);
                } else {
                    System.out.println("None of them is earlier than current one");
                }
            }
            TimeUnit.MINUTES.sleep(1L);
        }
    }
}
