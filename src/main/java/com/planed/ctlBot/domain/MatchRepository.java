package com.planed.ctlBot.domain;

import com.planed.ctlBot.data.MatchEntity;
import com.planed.ctlBot.data.UserEntity;
import com.planed.ctlBot.data.repositories.MatchEntityRepository;
import com.planed.ctlBot.data.repositories.UserEntityRepository;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MatchRepository {
    private final Mapper mapper = new DozerBeanMapper();

    private final MatchEntityRepository matchEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final UserRepository userRepository;

    @Autowired
    public MatchRepository(final MatchEntityRepository matchEntityRepository,
                           final UserEntityRepository userEntityRepository,
                           final UserRepository userRepository) {
        this.matchEntityRepository = matchEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.userRepository = userRepository;
    }

    public Match findMatchForUser(final String discordId) {
        final UserEntity user = userEntityRepository.findByDiscordId(discordId);
        final List<MatchEntity> resultList = new ArrayList<>();
        resultList.addAll(matchEntityRepository.findMatchByPlayers(user));
        if (resultList.size() == 1) {
            return mapper.map(resultList.get(0), Match.class);
        } else {
            return null;
        }
    }

    public void addMatch(final User author, final User challengee) {
        final List<User> playerList = new ArrayList<>();
        playerList.add(author);
        playerList.add(challengee);

        final Match match = new Match();
        match.setPlayers(playerList);
        final long matchId = matchEntityRepository.save(mapToEntity(match)).getMatchId();

        final Match dbMatch = findMatchById(matchId);
        playerList.forEach(p->{
            p.setMatch(dbMatch);
            userRepository.save(p);
        });
    }

    public Match findMatchById(final Long matchId) {
        return mapper.map(matchEntityRepository.findOne(matchId), Match.class);
    }

    private MatchEntity mapToEntity(final Match match) {
        final List<UserEntity> players = new ArrayList<>();
        match.getPlayers()
                .forEach(p -> {
                    players.add(
                            userEntityRepository.findOne(p.getUserId()));
                }
                );

        final MatchEntity result = mapper.map(match, MatchEntity.class);
        result.setPlayers(players);
        return result;
    }

    public void saveMatch(final Match match) {
        matchEntityRepository.save(mapper.map(match, MatchEntity.class));
    }
}
