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
public class ResetMatchCommand extends AbstractMatchCommand {
    private Logger LOG = LoggerFactory.getLogger(InterfaceListener.class);

    public static final String COMMAND_STRING = "resetMatch";

    @Autowired
    ResetMatchCommand(CtlMatchService ctlMatchService, BotCommandParser parser) {
        super(ctlMatchService);
        parser.register(COMMAND_STRING, this);
    }

    @Override
    public void manipulateMatch(MessageReceivedEvent event, CtlMatch match) {
        match.setWinner(null);
        LOG.info("Resetting winner on match " + match);
        replyInChannel(event, "Reset the winner in your match.");
    }

    @Override
    public String getHelpText() {
        return "Lets you reset your match-result in case you messed up.";
    }
}
