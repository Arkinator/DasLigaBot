package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.User;
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

    private final DiscordService discordService;

    @Autowired
    public BasicCommands(final DiscordService discordService) {
        this.discordService = discordService;
    }

    @DiscordCommand(name = "hello", help = "Hello World command")
    public void helloCommand(final CommandCall call) {
        LOG.info("received hello command from " + call.getAuthor());
        discordService.replyInChannel(call.getChannel(), "he yourself");
    }

    @DiscordCommand(name = {"list", "help"}, help = "Lists all available commands")
    public void listAllCommands(final CommandCall call) {
        LOG.info("received list command");
        discordService.replyInChannel(call.getChannel(), discordService.getCommandList());
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
}
