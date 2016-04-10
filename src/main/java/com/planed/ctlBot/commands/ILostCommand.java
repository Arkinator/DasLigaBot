package com.planed.ctlBot.commands;

import com.planed.ctlBot.InterfaceListener;
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
public class ILostCommand extends AbstractMatchCommand {
    private Logger LOG = LoggerFactory.getLogger(InterfaceListener.class);

    public static final String COMMAND_STRING = "ilost";

    @Autowired
    ILostCommand(CtlMatchService ctlMatchService, BotCommandParser parser){
        super(ctlMatchService);
        parser.register(COMMAND_STRING, this);
    }

    @Override
    public void manipulateMatch(MessageReceivedEvent event, CtlMatch match) {
        String author = getAuthor(event);
        if (author.equals(match.getPlayer1())) {
            match.setWinner(match.getPlayer2());
            exitLogging(event, match, match.getPlayer2());
        }else{
            match.setWinner(match.getPlayer1());
            exitLogging(event, match, match.getPlayer1());
        }
    }

    private void exitLogging(MessageReceivedEvent event, CtlMatch match, String winner) {
        LOG.info("Setting winner on match "+match+" to "+winner);
        replyInChannel(event, "You lost your match against "+winner+". Tough luck!");
    }

    @Override
    public String getHelpText() {
        return "This command signals that despite your best effort your match this week went awry";
    }
}
