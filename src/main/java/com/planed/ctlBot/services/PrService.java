package com.planed.ctlBot.services;

import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.Match;
import com.planed.ctlBot.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class PrService {
    private static final String CODE_ESCAPE = "```";
    private static final String matchFormatter =
            "\n*******************************************************************************\n" +
                    "  The showdown is real! A Challenge has been accepted, it is g0-time boisss!   \n" +
                    "   {0} ()==[:::::::::> VS <::::::::::||===) {1}     \n" +
                    "*******************************************************************************";
    private static final String extendedFormatter =
            "A challenge has been extended from {0}. Will {1} man up or will he wuzz out?";
    private static final String rejectedFormatter =
            "The honor of {1} has taken another hit. He rejected the challenge from {0}";
    private static final String resultFormatter =
            "\n*******************************************************************************\n" +
                    "             @         That just in:  \n" +
                    "          @:::@                    \n" +
                    "       @.:/\\:/\\:.@         {1}   \n" +
                    "      ':\\@ @ @ @/:'                      \n" +
                    "        [@W@W@W@]                 got beaten     \n" +
                    "        `\"\"\"\"\"\"\"`           by the glorious    \n" +
                    "          {0}       \n" +
                    "*******************************************************************************";

    @Value("${pr.channel.name:116271758763491336}")
    public String prChannelName;
    @Autowired
    public DiscordService discordService;

    public void printChallengeExtendedMessage(final Match match) {
        discordService.replyInChannel(match.getOriginatingServerId(), match.getOriginatingChannelId(), MessageFormat.format(extendedFormatter,
                match.getPlayerA().toString(),
                match.getPlayerB().toString()));
    }

    public void printGameIsOnMessage(final Match match) {
        String message = MessageFormat.format(matchFormatter,
                getPlayerName(match.getPlayerA(), match.getOriginatingServerId()),
                getPlayerName(match.getPlayerB(), match.getOriginatingServerId()));
        discordService.replyInChannel(match.getOriginatingServerId(), match.getOriginatingChannelId(), CODE_ESCAPE + message + CODE_ESCAPE);
    }

    public void printChallengeRejectedMessage(final Match match) {
        discordService.replyInChannel(match.getOriginatingServerId(), match.getOriginatingChannelId(),
                MessageFormat.format(rejectedFormatter,
                match.getPlayerA().toString(),
                match.getPlayerB().toString()));
    }

    private String getPlayerName(final User user, String serverId) {
        return discordService.getDiscordName(user.getDiscordId(), serverId);
    }

    public void printMessageResultMessage(final Match match) {
        final double outcome = match.getEloResult();
        String resultMessage = null;
        if (outcome < 0.1) {
            resultMessage = CODE_ESCAPE + MessageFormat.format(resultFormatter,
                    getPlayerName(match.getPlayerB(), match.getOriginatingServerId()),
                    getPlayerName(match.getPlayerA(), match.getOriginatingServerId())) + CODE_ESCAPE;
        } else if (outcome > 0.9) {
            resultMessage = CODE_ESCAPE + MessageFormat.format(resultFormatter,
                    getPlayerName(match.getPlayerA(), match.getOriginatingServerId()),
                    getPlayerName(match.getPlayerB(), match.getOriginatingServerId())) + CODE_ESCAPE;
        }
        discordService.replyInChannel(match.getOriginatingServerId(), match.getOriginatingChannelId(), resultMessage);
    }
}
