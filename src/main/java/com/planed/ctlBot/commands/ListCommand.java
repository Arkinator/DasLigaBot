package com.planed.ctlBot.commands;

import com.planed.ctlBot.data.User;
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
public class ListCommand extends AbstractBotCommand {
    private Logger LOG = LoggerFactory.getLogger(ListCommand.class);

    public static final String COMMAND_STRING = "list";
    private static final String DIVIDER = "\t";
    private BotCommandParser parser;
    private final UserService userService;

    @Autowired
    ListCommand(BotCommandParser parser, UserService userService){
        parser.register(COMMAND_STRING, this);
        this.parser = parser;
        this.userService = userService;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        User user = userService.getCurrentUser(event);
        final StringBuilder builder = new StringBuilder();
        parser.getAllRegisteredCommands().forEach(entry -> {
            if (entry.getValue().getAccessLevel().ordinal()<=user.getAccessLevel().ordinal()) {
                builder.append(entry.getKey());
                builder.append(DIVIDER);
                builder.append(entry.getValue().getHelpText());
                builder.append("\n");
            }
        });
        replyInChannel(event, builder.toString());
    }

    @Override
    public String getHelpText() {
        return "Lists all available commands";
    }
}
