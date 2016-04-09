package com.planed.ctlBot.commands;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class HelloWorldCommand extends AbstractBotCommand {
    public static final String COMMAND_STRING = "hello";
    private static final String HELLO_WORLD_MESSAGE = "Hello World!";

    HelloWorldCommand(MessageReceivedEvent event){
        super(event);
    }

    @Override
    public boolean isValidCommand() {
        return true;
    }

    @Override
    public String getCommandAsString() {
        return getMessage().getContent();
    }

    @Override
    public void execute() {
        try {
            getMessage().getChannel().sendMessage(HELLO_WORLD_MESSAGE);
        } catch (MissingPermissionsException e) {
            e.printStackTrace();
        } catch (HTTP429Exception e) {
            e.printStackTrace();
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }
}
