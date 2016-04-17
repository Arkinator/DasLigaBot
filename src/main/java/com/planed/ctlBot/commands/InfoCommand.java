package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.services.CommandService;
import com.planed.ctlBot.services.DiscordService;
import com.planed.ctlBot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class InfoCommand implements BotCommand {
    private static final String INFO_COMMAND_PHRASE = "info";
    private final UserService userService;
    private final DiscordService discordService;
    private final CommandService commandService;
    Logger LOG = LoggerFactory.getLogger(InfoCommand.class);

    @Autowired
    public InfoCommand(UserService userService, DiscordService discordService, CommandService commandService) {
        this.userService = userService;
        this.discordService = discordService;
        this.commandService = commandService;
        this.commandService.subscribe(this, INFO_COMMAND_PHRASE);
    }

    @Override
    public void messageReceiver(CommandCall command) {
        discordService.whisperToPerson(command.getAuthorId(), buildInfoMessage());
    }

    private String buildInfoMessage() {
        return "info";
    }
}
