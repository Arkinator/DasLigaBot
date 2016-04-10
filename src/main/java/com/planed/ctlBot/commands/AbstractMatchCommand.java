package com.planed.ctlBot.commands;

import com.planed.ctlBot.data.CtlMatch;
import com.planed.ctlBot.services.CtlMatchService;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.Optional;

import static org.eclipse.jetty.io.SelectChannelEndPoint.LOG;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public abstract class AbstractMatchCommand extends AbstractBotCommand {
    private final CtlMatchService ctlMatchService;

    public AbstractMatchCommand(final CtlMatchService ctlMatchService) {
        this.ctlMatchService = ctlMatchService;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        Optional<CtlMatch> matchOptional = findMatchInCurrentWeekForMessageAuthor(event);
        if (matchOptional.isPresent()){
            CtlMatch match = matchOptional.get();
            manipulateMatch(event, match);
            ctlMatchService.saveMatch(match);
        } else {
            replyInChannel(event, "Unable to find corresponding match! (Do you play this week?)");
            LOG.warn("No action executed! '"+getAuthor(event)+"' has no registered matches!");
        }
    }

    public Optional<CtlMatch> findMatchInCurrentWeekForMessageAuthor(MessageReceivedEvent event) {
        return ctlMatchService.findMatchInCurrentWeekForUser(getAuthor(event));
    }

    public abstract void manipulateMatch(MessageReceivedEvent event, CtlMatch match);
}
