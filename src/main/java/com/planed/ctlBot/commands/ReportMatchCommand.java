package com.planed.ctlBot.commands;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.data.CtlMatch;
import com.planed.ctlBot.services.CtlMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.Optional;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class ReportMatchCommand extends AbstractBotCommand {
    private Logger LOG = LoggerFactory.getLogger(ReportMatchCommand.class);

    public static final String COMMAND_STRING = "reportMatch";
    private CtlMatchService ctlMatchService;

    @Autowired
    ReportMatchCommand(CtlMatchService ctlMatchService, BotCommandParser parser) {
        parser.register(COMMAND_STRING, this);
        this.ctlMatchService = ctlMatchService;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String targetUser = getParams(event).get(0);
        Optional<CtlMatch> match = ctlMatchService.findMatchInCurrentWeekForUser(targetUser);
        if (match.isPresent()){
            if (getParams(event).contains("won") ||
                    getParams(event).contains("win") ||
                    getParams(event).contains("winner")) {
                match.get().setWinner(targetUser);
            } else if (getParams(event).contains("lost") ||
                    getParams(event).contains("loosing") ||
                    getParams(event).contains("looser")) {
                setOtherPlayerAsWinner(targetUser, match);
            }
            ctlMatchService.saveMatch(match.get());
        }
    }

    private void setOtherPlayerAsWinner(String targetUser, Optional<CtlMatch> match) {
        if (match.get().getPlayer1().equals(targetUser)) {
            match.get().setWinner(match.get().getPlayer2());
        } else if (match.get().getPlayer2().equals(targetUser)) {
            match.get().setWinner(match.get().getPlayer1());
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.Admin;
    }

    @Override
    public String getHelpText() {
        return "Admins can report results for other, lazier people";
    }
}
