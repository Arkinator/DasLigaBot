package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.discord.CommandRegistry;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.User;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Julian Peters on 23.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@DiscordController
public class BasicCommands {
    Logger LOG = LoggerFactory.getLogger(BasicCommands.class);

    @Autowired
    private DiscordService discordService;
    @Autowired
    private CommandRegistry commandRegistry;

    @DiscordCommand(name = "hello", help = "Hello World command")
    public void helloCommand(final CommandCall call) {
        LOG.info("received hello command from " + call.getAuthor());
        discordService.replyInChannel(call.getChannel(), "he yourself");
    }

    @DiscordCommand(name = {"list", "help"}, help = "Lists all available commands")
    public void listAllCommands(final CommandCall call) {
        LOG.info("received list command");
        discordService.replyInChannel(call.getChannel(), buildCommandList());
    }

    @DiscordCommand(name = {"info", "whataboutme"}, help = "Displays some information about yourself")
    public void infoCommand(final CommandCall call) {
        LOG.info("received info command");
        discordService.replyInChannel(call.getChannel(), buildInfoText(call));
    }

    private String buildInfoText(final CommandCall call) {
        final User user = call.getAuthor();
        return "You are " + user.getDiscordId() + " and had " + user.getNumberOfInteractions()
                + " interactions with me. You clearance-level is "
                + user.getAccessLevel() + ". Have a nice day!";
    }

    private String buildCommandList() {
        final StringBuilder builder = new StringBuilder();
        for (final DiscordCommand command : commandRegistry.getAllCommands()) {
            builder.append(ArrayUtils.toString(command.name()));
            builder.append("\t\t");
            builder.append(command.help());
            builder.append("\n");
        }
        return builder.toString();
    }
}
