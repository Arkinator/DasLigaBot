package com.planed.ctlBot.services.executors;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.spi.JsonException;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Builder
@AllArgsConstructor
public class BattleNetProfileRetriever {
    private static final Logger logger = LoggerFactory.getLogger(BattleNetProfileRetriever.class);

    private String battleNetId;
    private String tokenValue;

    public Pair<String, Long> execute() {
        Unirest.setDefaultHeader("Authorization", "Bearer " + tokenValue);

        List<Any> profileInfoObjects = retrieveUrlOptionalSafe("https://eu.api.blizzard.com/sc2/player/" + battleNetId)
                .map(playerSummary -> playerSummary.asList())
                .get();

        return profileInfoObjects.parallelStream()
                .map(infoObject -> mapLadderInfoToInfo(infoObject))
                .filter(Optional::isPresent).map(Optional::get)
                .sorted((p1, p2) -> p2.getRight().compareTo(p1.getRight()))
                .findFirst()
                .get();
    }

    private Optional<Pair<String, Long>> mapLadderInfoToInfo(Any any) {
        String regionId = any.toString("regionId");
        String realmId = any.toString("realmId");
        String profileId = any.toString("profileId");

        String baseUrlLadder = "https://eu.api.blizzard.com/sc2/profile/" + regionId + "/" + realmId + "/" + profileId + "/ladder/";
        return retrieveUrlOptionalSafe(baseUrlLadder + "summary")
                .flatMap(ladderInfo -> getHighestRankedLadderForPlayedId(ladderInfo, baseUrlLadder, profileId));
    }

    private Optional<Pair<String, Long>> getHighestRankedLadderForPlayedId(Any ladderInfo, String baseUrlLadder, String profileId) {
        return ladderInfo.get("allLadderMemberships").asList().parallelStream()
                .map(entry -> classifyLadderEntry(entry, baseUrlLadder, profileId))
                .filter(Optional::isPresent).map(Optional::get)
                .sorted((p1, p2) -> p2.getRight().compareTo(p1.getRight()))
                .findFirst();
    }

    private Optional<Pair<String, Long>> classifyLadderEntry(Any ladderEntry, String baseUrlLadder, String profileId) {
        if (!ladderEntry.toString("localizedGameMode").startsWith("1v1")) {
            return Optional.empty();
        }
        logger.info("Querying " + ladderEntry.toString("localizedGameMode"));
        String ladderId = ladderEntry.toString("ladderId");
        String url = baseUrlLadder + ladderId;
        final Optional<Any> ladderInfoOptional = retrieveUrlOptionalSafe(url);
        if (!ladderInfoOptional.isPresent()) {
            return Optional.empty();
        }

        Long mmr = ladderInfoOptional.get().toLong("ranksAndPools", 0, "mmr");
        final Optional<String> raceFromLadder = getRaceFromLadder(ladderInfoOptional.get(), profileId);
        if (!raceFromLadder.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(Pair.of(raceFromLadder.get(), mmr));
    }

    private Optional<String> getRaceFromLadder(Any ladder, String profileId) {
        return ladder.get("ladderTeams").asList().stream()
                .map(team -> team.get("teamMembers", 0))
                .filter(teamMember -> profileId.equals(teamMember.toString("id")))
                .map(teamMember -> teamMember.toString("favoriteRace"))
                .findFirst();
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
