package com.planed.ctlBot.services;

import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.executors.BattleNetProfileRetriever;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

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
        Pair<String, Long> detailsOfBestLeagueOfUser = BattleNetProfileRetriever.builder()
                .battleNetId(battleNetId)
                .tokenValue(tokenValue)
                .build()
                .execute();

        final String newRace = detailsOfBestLeagueOfUser.getLeft();
        final Long maxMmr = detailsOfBestLeagueOfUser.getRight();
        final User user = userService.updateUserLeagueInformation(discordId, newRace, maxMmr);
        double newElo = user.getElo();

        final String message = MessageFormat.format(UPDATE_MESSAGE_BLUEPRINT, newRace, maxMmr, newElo);
        discordService.whisperToUser(discordId, message);
    }
}
