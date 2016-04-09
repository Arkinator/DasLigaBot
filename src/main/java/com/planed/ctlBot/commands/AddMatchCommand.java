package com.planed.ctlBot.commands;

import com.planed.ctlBot.CtlDataStore;
import org.springframework.util.Assert;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class AddMatchCommand extends AbstractBotCommand {
    public static final String COMMAND_STRING = "addMatch";
    public CtlDataStore ctlDataStore;

    AddMatchCommand(CtlDataStore ctlDataStore, MessageReceivedEvent event){
        super(event);
        this.ctlDataStore = ctlDataStore;
    }

    @Override
    public void execute() {
        List<IUser> mentions = getMessage().getMentions();
        Assert.isTrue(mentions.size()==2);
        ctlDataStore.addMatch(mentions.get(0), mentions.get(1));
    }
}
