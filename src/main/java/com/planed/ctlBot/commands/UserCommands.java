package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Created by Julian Peters on 23.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@DiscordController
public class UserCommands {
    Logger LOG = LoggerFactory.getLogger(UserCommands.class);

    @Autowired
    private UserService userService;
    @Autowired
    private DiscordService discordService;

    @DiscordCommand(name = {"setRace","changeRace"}, help = "This command lets you change your race. Specify whether you play Zerg, Terran, Protoss or Random")
    public void changeRaceCommand(final CommandCall call) {
        Assert.isTrue(call.getParameters().size() >= 1);
        userService.changeRace(call.getAuthor(), call.getParameters().get(0));
    }

    @DiscordCommand(name = {"challenge"}, help = "")
    public void issueChallenge(final CommandCall call) {
        Assert.isTrue(call.getMentions().size() >= 1);
        final User challenger = call.getAuthor();
        final User challengee = call.getMentions().get(0);
        if (challenger.getMatch() != null) {
            discordService.replyInChannel(call.getChannel(), "You are already in a match: '"+challenger.getMatch());
        } else if (challengee.getMatch() != null) {
            discordService.replyInChannel(call.getChannel(), "The challengee is already in a match: '"+challengee);
        } else {
            userService.issueChallenge(challenger, challengee);
        }
    }

    @DiscordCommand(name = {"reject"}, help = "")
    public void rejectChallenge(final CommandCall call) {
        final User author = call.getAuthor();
        if (author.getUserId() == author.getMatch().getPlayers().get(0).getUserId()){
            discordService.replyInChannel(call.getChannel(), "You can not reject a challenge you did not make! Your current match is "+author.getMatch());
        }else {
            userService.rejectChallenge(call.getAuthor());
        }
    }
}
