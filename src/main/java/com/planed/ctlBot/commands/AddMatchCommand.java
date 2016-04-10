package com.planed.ctlBot.commands;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.data.CtlMatch;
import com.planed.ctlBot.data.repositories.CtlMatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.util.List;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class AddMatchCommand extends AbstractBotCommand {
    private Logger LOG = LoggerFactory.getLogger(AddMatchCommand.class);

    public static final String COMMAND_STRING = "addMatch";
    private final CtlMatchRepository ctlMatchRepository;

    @Autowired
    AddMatchCommand(BotCommandParser parser, CtlMatchRepository ctlMatchRepository){
        parser.register(COMMAND_STRING, this);
        this.ctlMatchRepository = ctlMatchRepository;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        List<String> userNames = getParams(event);
        LOG.info("Call to addMatchCommand.execute(). The params are "+userNames);
        Assert.isTrue(userNames.size() == 2);
        ctlMatchRepository.save(new CtlMatch(userNames.get(0),userNames.get(1)));
        replyInChannel(event, "Match added between '"+userNames.get(0)+"' and '"+userNames.get(1)+"'");
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.Admin;
    }

    @Override
    public String getHelpText() {
        return "Admins: you can add a new match with this. Just link the UR-Player (with @), space, and enter the name of the opponent (no spaces in the name)";
    }
}
