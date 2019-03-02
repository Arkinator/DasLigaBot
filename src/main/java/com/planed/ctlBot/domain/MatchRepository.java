package com.planed.ctlBot.domain;

import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.data.MatchEntity;
import com.planed.ctlBot.data.repositories.MatchEntityRepository;
import com.planed.ctlBot.data.repositories.UserEntityRepository;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MatchRepository {
    private final Mapper mapper = new DozerBeanMapper();

    @Autowired
    private MatchEntityRepository matchEntityRepository;
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private UserRepository userRepository;

    public Optional<Match> findMatchForUser(final String discordId) {
        // TODO change to optional and java9 .or(<other optional>)
        final List<MatchEntity> resultList = new ArrayList<>();
        resultList.addAll(matchEntityRepository.findMatchByPlayerA(discordId));
        resultList.addAll(matchEntityRepository.findMatchByPlayerB(discordId));
        if (resultList.size() == 1) {
            return Optional.of(mapper.map(resultList.get(0), Match.class));
        } else {
            return Optional.empty();
        }
    }

    public Match addMatch(final User author, final User challengee, String serverId, String channelId) {
        final List<User> playerList = new ArrayList<>();
        playerList.add(author);
        playerList.add(challengee);

        final Match match = new Match();
        match.setPlayers(playerList);
        match.setGameStatus(GameStatus.CHALLENGE_EXTENDED);
        match.setOriginatingServerId(serverId);
        match.setOriginatingChannelId(channelId);
        final long matchId = matchEntityRepository.save(mapToEntity(match)).getMatchId();

        return findMatchById(matchId)
                .orElseThrow(() -> new RuntimeException("Error while adding match: Could not find newly created match in DB!"));
    }

    public Optional<Match> findMatchById(final Long matchId) {
        return Optional.ofNullable(matchId)
                .flatMap(id -> matchEntityRepository.findById(id))
                .map(match -> mapperFromEntity(match));
    }

    private Match mapperFromEntity(final MatchEntity match) {
        final Match result = mapper.map(match, Match.class);
        result.setPlayers(new ArrayList<>());
        result.getPlayers().add(userRepository.findByDiscordId(match.getPlayerA()));
        result.getPlayers().add(userRepository.findByDiscordId(match.getPlayerB()));
        return result;
    }

    private MatchEntity mapToEntity(final Match match) {
        final MatchEntity result = mapper.map(match, MatchEntity.class);
        if (match.getPlayerA() != null) {
            result.setPlayerA(match.getPlayerA().getDiscordId());
        }
        if (match.getPlayerB() != null) {
            result.setPlayerB(match.getPlayerB().getDiscordId());
        }
        return result;
    }

    public void saveMatch(final Match match) {
        matchEntityRepository.save(mapToEntity(match));
    }

    public List<Match> findAll() {
        final List<Match> result = new ArrayList<>();
        matchEntityRepository.findAll().forEach(m -> result.add(mapperFromEntity(m)));
        return result;
    }
}
