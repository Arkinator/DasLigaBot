package com.planed.ctlBot.commands;

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
public class StandingsCommand extends AbstractBotCommand {
    private Logger LOG = LoggerFactory.getLogger(StandingsCommand.class);

    public static final String COMMAND_STRING = "standings";
    private CtlMatchService ctlMatchService;

    @Autowired
    StandingsCommand(CtlMatchService ctlMatchService, BotCommandParser parser) {
        parser.register(COMMAND_STRING, this);
        this.ctlMatchService = ctlMatchService;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        final StringBuilder builder = new StringBuilder();
        ctlMatchService.findAllMatchesInCurrentWeek().forEach(match -> {
            builder.append(match.getPlayer1());
            builder.append(" vs ");
            builder.append(match.getPlayer2());
            builder.append(" => ");
            if (match.getWinner() == null) {
                builder.append("no result submitted yet!");
            } else {
                builder.append(match.getWinner());
            }
            builder.append("\n");
        });
        if (builder.length() == 0) {
            replyInChannel(event, "No matches found!");
        } else {
            replyInChannel(event, builder.toString());
        }
    }

    @Override
    public String getHelpText() {
        return "Outputs the current CTL standings";
    }
}
