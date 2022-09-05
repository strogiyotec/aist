package org.aist.telegram;

import java.time.Duration;
import java.time.LocalDate;

public interface TelegramBotCommands {

    void publishEarliestDate(LocalDate localDate) throws Exception;

    void publishCurrentDate(LocalDate localDate, Duration duration) throws Exception;

    void publishNoAppointments(final Duration duration) throws Exception;
}
