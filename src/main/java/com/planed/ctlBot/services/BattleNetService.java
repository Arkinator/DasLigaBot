package com.planed.ctlBot.services;

import com.planed.ctlBot.common.League;
import com.planed.ctlBot.common.Race;
import com.planed.ctlBot.data.BattleNetInformation;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.executors.BattleNetProfileRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BattleNetService {
    private static final Logger logger = LoggerFactory.getLogger(BattleNetService.class);
    private static final String UPDATE_MESSAGE_BLUEPRINT = "Your battle.net account was successfully linked to Liga-Account. " +
            "You apparently play as {0} with an MMR of {1}. Your corrected ELO was set at {2}";

    @Autowired
    private UserService userService;
    @Autowired
    private DiscordService discordService;

    @Async
    public void retrieveAndSafeUserLadderInformation(String battleNetId, String tokenValue, String discordId) {
        logger.info("Starting battle.net query for user with battleNetId '" + battleNetId + "' and token Value '" + tokenValue + "'...");
        BattleNetInformation detailsOfBestLeagueOfUser = BattleNetProfileRetriever.builder()
                .battleNetId(battleNetId)
                .tokenValue(tokenValue)
                .build()
                .execute();

        final User user = userService.updateUserLeagueInformation(discordId, detailsOfBestLeagueOfUser);
        double newElo = user.getElo();

        discordService.removeRolesFromUser(discordId, Stream.of(League.values())
                .map(Object::toString)
                .collect(Collectors.toList()));
        discordService.removeRolesFromUser(discordId, Stream.of(Race.values())
                .map(Object::toString)
                .collect(Collectors.toList()));
        discordService.addRoleToUser(discordId, detailsOfBestLeagueOfUser.getLeague().toString());
        discordService.addRoleToUser(discordId, detailsOfBestLeagueOfUser.getRace().toString());

        final String message = MessageFormat.format(UPDATE_MESSAGE_BLUEPRINT, detailsOfBestLeagueOfUser.getRace(), detailsOfBestLeagueOfUser.getMmr(), newElo);
        discordService.whisperToUser(discordId, message);
        logger.info("Finished battle.net query for user with battleNetId '" + battleNetId + "'!");
    }
}
