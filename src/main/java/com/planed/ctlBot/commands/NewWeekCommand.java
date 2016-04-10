package com.planed.ctlBot.commands;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.services.CtlMatchService;
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
public class NewWeekCommand extends AbstractBotCommand {
    private Logger LOG = LoggerFactory.getLogger(NewWeekCommand.class);

    public static final String COMMAND_STRING = "newWeek";
    private CtlMatchService ctlMatchService;

    @Autowired
    NewWeekCommand(CtlMatchService ctlMatchService, BotCommandParser parser) {
        parser.register(COMMAND_STRING, this);
        this.ctlMatchService = ctlMatchService;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        ctlMatchService.deleteAllMatches();
        replyInChannel(event,"All Matches have been deleted. Good luck in the new week!");
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.Admin;
    }

    @Override
    public String getHelpText() {
        return "Only Admins can start a new week with this command. Deletes all matches, can not be undone!";
    }
}
