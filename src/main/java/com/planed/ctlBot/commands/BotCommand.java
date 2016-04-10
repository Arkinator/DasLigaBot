package com.planed.ctlBot.commands;

import com.planed.ctlBot.common.AccessLevel;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public interface BotCommand {
    void execute(MessageReceivedEvent event);

    AccessLevel getAccessLevel();

    boolean doesUserHaveNecessaryLevel(MessageReceivedEvent event);

    String getHelpText();
}
