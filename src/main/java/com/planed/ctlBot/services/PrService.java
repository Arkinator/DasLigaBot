package com.planed.ctlBot.services;

import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.Match;
import com.planed.ctlBot.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Created by Julian Peters on 01.05.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class PrService {
    private static final String CODE_ESCAPE = "```";
    private static final String matchFormatter =
            CODE_ESCAPE+"\n*******************************************************************************\n" +
                    "  The showdown is real! A Challenge has been accepted, it's g0-time boisss!\n" +
                    "   {0} ()==[:::::::::> VS <::::::::::||===) {1}\n" +
                    "*******************************************************************************"+CODE_ESCAPE;
    private static final String extendedFormatter =
            "A challenge has been extended from {0}. Will {1} man up or will he wuzz out?";
    private static final String rejectedFormatter =
            "The honor of {1} has taken another hit. He rejected the challenge from {0}";
    private static final String resultFormatter =
            CODE_ESCAPE+"\n*******************************************************************************\n" +
                    "             @         Thats just in:  \n" +
                    "          @:::@                    \n" +
                    "       @.:/\\:/\\:.@         {1}   \n" +
                    "      ':\\@ @ @ @/:'                      \n" +
                    "        [@W@W@W@]                 got beaten     \n" +
                    "        `\"\"\"\"\"\"\"`           by the glorious    \n" +
                    "          {0}       \n" +
                    "*******************************************************************************"+CODE_ESCAPE;

    @Value("${pr.channel.name:116271758763491336}")
    public String prChannelName;
    @Autowired
    public DiscordService discordService;

    public void printChallengeExtendedMessage(final Match match) {
        discordService.replyInChannel(prChannelName, MessageFormat.format(extendedFormatter,
                match.getPlayerA().toString(),
                match.getPlayerB().toString()));
    }

    public void printGameIsOnMessage(final Match match) {
        discordService.replyInChannel(prChannelName, MessageFormat.format(matchFormatter,
                getPlayerName(match.getPlayerA()),
                getPlayerName(match.getPlayerB())));
    }

    public void printChallengeRejectedMessage(final Match match) {
        discordService.replyInChannel(prChannelName, MessageFormat.format(rejectedFormatter,
                match.getPlayerA().toString(),
                match.getPlayerB().toString()));
    }

    private String getPlayerName(final User user) {
        return discordService.getDiscordName(user);
    }

    public void printMessageResultMessage(final Match match) {
        final double outcome = match.getEloResult();
        if (outcome < 0.1) {
            discordService.replyInChannel(prChannelName, MessageFormat.format(resultFormatter,
                    getPlayerName(match.getPlayerB()),
                    getPlayerName(match.getPlayerA())));
        } else if (outcome > 0.9) {
            discordService.replyInChannel(prChannelName, MessageFormat.format(resultFormatter,
                    getPlayerName(match.getPlayerA()),
                    getPlayerName(match.getPlayerB())));
        }
    }
}
