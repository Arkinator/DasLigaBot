package com.planed.ctlBot.commands;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public abstract class AbstractBotCommand implements BotCommand {
    private final MessageReceivedEvent event;

    public AbstractBotCommand(MessageReceivedEvent event){
        this.event = event;
    }

    public IMessage getMessage() {
        return event.getMessage();
    }

    @Override
    public boolean isValidCommand() {
        return true;
    }

    @Override
    public String getCommandAsString() {
        return event.getMessage().getContent();
    }
}
