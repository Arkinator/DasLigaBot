package com.planed.ctlBot.commands;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.List;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class GiveAdminCommand extends AbstractBotCommand {
    private final UserService userService;
    private Logger LOG = LoggerFactory.getLogger(GiveAdminCommand.class);

    public static final String COMMAND_STRING = "giveAdmin";
    private BotCommandParser parser;

    @Autowired
    GiveAdminCommand(BotCommandParser parser, UserService userService) {
        parser.register(COMMAND_STRING, this);
        this.parser = parser;
        this.userService = userService;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        List<String> userNames = getParams(event);
        if (userNames.size() == 1 && userDoesExist(event, userNames.get(0))) {
            userService.giveUserAccessLevel(userNames.get(0), AccessLevel.Admin);
            replyInChannel(event, "Promoted user "+userService.getNameForId(event, userNames.get(0))+" to Admin");
        }
    }

    private boolean userDoesExist(MessageReceivedEvent event, String discordId) {
        return event.getClient().getUserByID(userService.getConvertedDiscordId(discordId)) != null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.Author;
    }
}
