package com.planed.ctlBot.commands;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.data.User;
import com.planed.ctlBot.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public abstract class AbstractBotCommand implements BotCommand {

    @Autowired
    private UserRepository userRepository;

    AbstractBotCommand() {

    }

    public void replyInChannel(MessageReceivedEvent event, String message) {
        try {
            event.getMessage().getChannel().sendMessage(message);
        } catch (MissingPermissionsException | HTTP429Exception | DiscordException e) {
            e.printStackTrace();
        }
    }

    public List<String> getParams(MessageReceivedEvent event) {
        String trimmedContent = event.getMessage().getContent().trim();
        List<String> arguments = new ArrayList<>(Arrays.asList(trimmedContent.split(" ")));
        arguments.remove(0);
        return arguments;
    }

    public String getAuthor(MessageReceivedEvent event) {
        return "<@" + event.getMessage().getAuthor().getID() + ">";
    }

    @Override
    public boolean doesUserHaveNecessaryLevel(MessageReceivedEvent event) {
        User user = userRepository.findByDiscordId(getAuthor(event));
        if (user == null) {
            user = new User();
            user.setDiscordId(getAuthor(event));
            user = userRepository.save(user);
        }
        if (user.getAccessLevel().ordinal() >= getAccessLevel().ordinal()) {
            user.setNumberOfInteractions(user.getNumberOfInteractions() + 1);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.User;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
