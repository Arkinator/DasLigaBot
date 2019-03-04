package com.planed.ctlBot.services;

import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.Match;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.utils.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class PrService {
    private static final Logger logger = LoggerFactory.getLogger(PrService.class);

    @Value("${pr.channel.name:116271758763491336}")
    public String prChannelName;
    @Autowired
    public DiscordService discordService;
    @Autowired
    private ServerService serverService;

    public void printChallengeExtendedMessage(final Match match) {
        findAnnouncerChannelForServer(match.getOriginatingServerId())
                .ifPresent(announcerChannelForServer ->
                        discordService.replyInChannel(match.getOriginatingServerId(), announcerChannelForServer,
                                MessageFormat.format(StringConstants.EXTENDED_FORMATTER,
                                        match.getPlayerA().toString(),
                                        match.getPlayerB().toString())));
    }

    public void printGameIsOnMessage(final Match match) {
        findAnnouncerChannelForServer(match.getOriginatingServerId())
                .ifPresent(announcerChannelForServer -> {
                    String message = StringConstants.CODE_ESCAPE + MessageFormat.format(StringConstants.MATCH_FORMATTER,
                            getPlayerName(match.getPlayerA(), match.getOriginatingServerId()),
                            getPlayerName(match.getPlayerB(), match.getOriginatingServerId())) + StringConstants.CODE_ESCAPE;
                    discordService.replyInChannel(match.getOriginatingServerId(), announcerChannelForServer, message);
                });
    }

    public void printChallengeRejectedMessage(final Match match) {
        findAnnouncerChannelForServer(match.getOriginatingServerId())
                .ifPresent(announcerChannelForServer ->
                        discordService.replyInChannel(match.getOriginatingServerId(), announcerChannelForServer,
                                MessageFormat.format(StringConstants.REJECTED_FORMATTER,
                                        match.getPlayerA().toString(),
                                        match.getPlayerB().toString())));
    }

    public void printMessageResultMessage(final Match match) {
        findAnnouncerChannelForServer(match.getOriginatingServerId())
                .ifPresent(announcerChannelForServer -> {
                    final double outcome = match.getEloResult();
                    String resultMessage = null;
                    if (outcome < 0.1) {
                        resultMessage = StringConstants.CODE_ESCAPE + MessageFormat.format(StringConstants.RESULT_FORMATTER,
                                getPlayerName(match.getPlayerB(), match.getOriginatingServerId()),
                                getPlayerName(match.getPlayerA(), match.getOriginatingServerId())) + StringConstants.CODE_ESCAPE;
                    } else if (outcome > 0.9) {
                        resultMessage = StringConstants.CODE_ESCAPE + MessageFormat.format(StringConstants.RESULT_FORMATTER,
                                getPlayerName(match.getPlayerA(), match.getOriginatingServerId()),
                                getPlayerName(match.getPlayerB(), match.getOriginatingServerId())) + StringConstants.CODE_ESCAPE;
                    }
                    discordService.replyInChannel(match.getOriginatingServerId(), announcerChannelForServer, resultMessage);
                });
    }

    private Optional<Long> findAnnouncerChannelForServer(Long serverId) {
        final Optional<Long> announcerChannelIdForServer = serverService.findAnnouncerChannelIdForServer(serverId);
        if (!announcerChannelIdForServer.isPresent()) {
            logger.warn("Unable to find announcer channel for server " + serverId + ". Maybe the bot was kicked?");
        }

        return announcerChannelIdForServer;
    }

    private String getPlayerName(final User user, Long serverId) {
        return discordService.getDiscordName(user.getDiscordId(), serverId);
    }
}
