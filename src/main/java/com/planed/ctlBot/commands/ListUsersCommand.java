package com.planed.ctlBot.commands;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class ListUsersCommand extends AbstractBotCommand {
    private Logger LOG = LoggerFactory.getLogger(ListUsersCommand.class);

    public static final String COMMAND_STRING = "listUsers";
    private UserService userService;

    @Autowired
    ListUsersCommand(UserService userService, BotCommandParser parser) {
        parser.register(COMMAND_STRING, this);
        this.userService = userService;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        final StringBuilder builder = new StringBuilder();
        userService.getAllUsers().forEach(user -> {
            builder.append(userService.getNameForId(event, user.getDiscordId()));
            builder.append("(");
            builder.append(user.getDiscordId());
            builder.append(") ");
            builder.append(user.getAccessLevel());
            builder.append(", #interactions=");
            builder.append(user.getNumberOfInteractions());
            builder.append("\n");
        });
        if (builder.length() == 0) {
            replyInChannel(event, "");
        } else {
            replyInChannel(event, builder.toString());
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.Author;
    }
}
