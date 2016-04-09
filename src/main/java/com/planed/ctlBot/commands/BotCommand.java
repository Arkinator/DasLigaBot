package com.planed.ctlBot.commands;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public interface BotCommand {
    boolean isValidCommand();

    String getCommandAsString();

    void execute();
}
