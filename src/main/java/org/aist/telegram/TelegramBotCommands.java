package org.aist.telegram;

import java.time.LocalDate;

public interface TelegramBotCommands {

    void publishEarliestDate(LocalDate localDate) throws Exception;

    String getLastMessage();
}
