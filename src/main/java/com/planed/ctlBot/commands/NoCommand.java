package com.planed.ctlBot.commands;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class NoCommand extends AbstractBotCommand {
    public NoCommand(MessageReceivedEvent message) {
        super(message);
    }

    @Override
    public boolean isValidCommand() {
        return false;
    }

    @Override
    public String getCommandAsString() {
        return null;
    }

    @Override
    public void execute() {

    }
}
