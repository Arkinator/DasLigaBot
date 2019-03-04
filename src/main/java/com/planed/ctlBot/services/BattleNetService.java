package com.planed.ctlBot.services;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.spi.JsonException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BattleNetService {
    private static final Logger logger = LoggerFactory.getLogger(BattleNetService.class);

    @Async
    public void retrieveAndSafeUserLadderInformation(String battleNetId) {
        List<Any> profileInfoObjects = retrieveUrlOptionalSafe("https://eu.api.blizzard.com/sc2/player/" + battleNetId)
                .map(playerSummary -> playerSummary.asList())
                .get();

        Any ladderInformationToSave = profileInfoObjects.parallelStream()
                .map(infoObject -> mapLadderInfoToInfo(infoObject))
                .filter(Optional::isPresent).map(Optional::get)
                .sorted((p1, p2) -> p2.getRight().compareTo(p1.getRight()))
                .findFirst()
                .map(pair -> pair.getLeft())
                .get();
//TODO in die zeile darüber muss wieder optional.map
        Long mmr = ladderInformationToSave.toLong("ranksAndPools", 0, "mmr");
        System.out.println(ladderInformationToSave);
    }

    private Optional<Pair<Any, Long>> mapLadderInfoToInfo(Any any) {
        String regionId = any.toString("regionId");
        String realmId = any.toString("realmId");
        String profileId = any.toString("profileId");

        String baseUrlLadder = "https://eu.api.blizzard.com/sc2/profile/" + regionId + "/" + realmId + "/" + profileId + "/ladder/";
        return retrieveUrlOptionalSafe(baseUrlLadder + "summary")
                .flatMap(ladderInfo -> getHighestRankedLadderForPlayedId(ladderInfo, baseUrlLadder));
    }

    private Optional<Pair<Any, Long>> getHighestRankedLadderForPlayedId(Any ladderInfo, String baseUrlLadder) {
        return ladderInfo.get("allLadderMemberships").asList().parallelStream()
                .map(entry -> classifyLadderEntry(entry, baseUrlLadder))
                .filter(Optional::isPresent).map(Optional::get)
                .sorted((p1, p2) -> p2.getRight().compareTo(p1.getRight()))
                .findFirst();
    }

    private Optional<Pair<Any, Long>> classifyLadderEntry(Any ladderEntry, String baseUrlLadder) {
        if (!ladderEntry.toString("localizedGameMode").startsWith("1v1")) {
            return Optional.empty();
        }
        logger.info("Querying " + ladderEntry.toString("localizedGameMode"));
        String ladderId = ladderEntry.toString("ladderId");
        String url = baseUrlLadder + ladderId;
        return retrieveUrlOptionalSafe(url)
                .map(ladder -> {
                    Long mmr = ladder.toLong("ranksAndPools", 0, "mmr");
                    return Pair.of(ladder, mmr);
                });
    }

    private Optional<Any> retrieveUrlOptionalSafe(String url) {
        try {
            logger.debug("Retrieving " + url);
            return Optional.of(JsonIterator.deserialize(Unirest.get(url).asString().getBody()));
        } catch (UnirestException | JsonException e) {
            logger.warn("Error while retrieving url '" + url + "'", e);
            return Optional.empty();
        }
    }
}
