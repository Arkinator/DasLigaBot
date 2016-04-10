package com.planed.ctlBot.commands;

import com.planed.ctlBot.data.CtlMatch;
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
public class ScoreCommand extends AbstractBotCommand {
    private Logger LOG = LoggerFactory.getLogger(ScoreCommand.class);

    public static final String COMMAND_STRING = "score";
    private CtlMatchService ctlMatchService;

    @Autowired
    ScoreCommand(CtlMatchService ctlMatchService, BotCommandParser parser) {
        parser.register(COMMAND_STRING, this);
        this.ctlMatchService = ctlMatchService;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        final StringBuilder builder = new StringBuilder();
        int scoreTeamA = 0;
        int scoreTeamB = 0;
        for (CtlMatch match : ctlMatchService.findAllMatchesInCurrentWeek()) {
            if (match.getPlayer1().equals(match.getWinner())) {
                scoreTeamA++;
            } else if (match.getPlayer2().equals(match.getWinner())) {
                scoreTeamB++;
            }
        }
        replyInChannel(event, "The score is " + scoreTeamA + " - " + scoreTeamB);
    }

    @Override
    public String getHelpText() {
        return "Outputs the current CTL score";
    }
}
