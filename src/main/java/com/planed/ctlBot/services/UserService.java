package com.planed.ctlBot.services;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.GameResult;
import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.common.LigaConstants;
import com.planed.ctlBot.common.Race;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.Match;
import com.planed.ctlBot.domain.MatchRepository;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {
    private final DiscordService discordService;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final PrService prService;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final DiscordService discordService,
                       final MatchRepository matchRepository,
                       final PrService prService) {
        this.userRepository = userRepository;
        this.discordService = discordService;
        this.matchRepository = matchRepository;
        this.prService = prService;
    }

    public void giveUserAccessLevel(final String discordId, final AccessLevel accessLevel) {
        User user = findUserAndCreateIfNotFound(discordId);
        user.setAccessLevel(accessLevel);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        final List<User> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);
        return result;
    }

    public User findUserAndCreateIfNotFound(final String discordId) {
        User result = userRepository.findByDiscordId(discordId);
        if (result == null) {
            addNewUser(discordId);
            result = userRepository.findByDiscordId(discordId);
        }
        return result;
    }

    public void addNewUser(final String discordId) {
        final User entity = new User();
        entity.setDiscordId(discordId);
        userRepository.save(entity);
    }

    public void changeRace(final User author, final String newRace) {
        author.setRace(Race.valueOf(newRace.toUpperCase()));
        userRepository.save(author);
    }

    public void issueChallenge(final User author, final User challengee, String serverId, String channelId) {
        final Match match = matchRepository.addMatch(author, challengee, serverId, channelId);
        author.setMatchId(match.getMatchId());
        challengee.setMatchId(match.getMatchId());
        userRepository.save(author, challengee);
        discordService.whisperToUser(challengee.getDiscordId(),
                "You have been challenged by "
                        + discordService.shortInfo(author)
                        + ". You can !reject or !accept the challenge");
        prService.printChallengeExtendedMessage(match);
    }

    public void rejectChallenge(final User author) {
        final Match match = matchRepository.findMatchById(author.getMatchId());
        prService.printChallengeRejectedMessage(match);
        match.setGameStatus(GameStatus.CHALLENGE_REJECTED);
        matchRepository.saveMatch(match);
        clearMatchOfInvolvedPlayers(match);
    }

    public void revokeChallenge(final User author) {
        final Match match = matchRepository.findMatchById(author.getMatchId());
        match.setGameStatus(GameStatus.CHALLENGE_REVOKED);
        matchRepository.saveMatch(match);
        clearMatchOfInvolvedPlayers(match);
        discordService.whisperToUser(author.getDiscordId(), "Your challenge has been revoked.");
        discordService.whisperToUser(match.getPlayerB().getDiscordId(),
                "The challenge from " + author.toString() + " has just been revoked.");
    }

    public void acceptChallenge(final User author) {
        final Match match = matchRepository.findMatchById(author.getMatchId());
        prService.printGameIsOnMessage(match);
        match.setGameStatus(GameStatus.CHALLENGE_ACCEPTED);
        matchRepository.saveMatch(match);
    }

    public void reportResult(final Match match, final User author, final GameResult result) {
        match.reportResult(author, result);
        if (match.getGameStatus() == GameStatus.GAME_PLAYED) {
            prService.printMessageResultMessage(match);
            finalizeGame(match);
        }
        matchRepository.saveMatch(match);
    }

    private void finalizeGame(final Match match) {
        if (match.getGameStatus() == GameStatus.GAME_PLAYED) {
            final User playerA = match.getPlayerA();
            final User playerB = match.getPlayerB();

            final double expectancy = 1 / (1 + Math.pow(10,
                    (playerB.getElo() - playerA.getElo()) / 400.));
            final double result = match.getEloResult();

            playerA.setElo(playerA.getElo() + LigaConstants.K_FACTOR * (result - expectancy));
            playerB.setElo(playerB.getElo() + LigaConstants.K_FACTOR * ((1 - result) - (1 - expectancy)));

            userRepository.save(playerA, playerB);
            clearMatchOfInvolvedPlayers(match);
        }
    }

    private void clearMatchOfInvolvedPlayers(final Match match) {
        match.getPlayers().forEach(p -> {
            p.setMatchId(null);
            userRepository.save(p);
        });
    }

    public String getLeagueString() {
        final StringBuilder result = new StringBuilder();
        final List<User> users = userRepository.findAll();
        users.sort((u1, u2) -> u1.getElo().compareTo(u2.getElo()));
        users.forEach(user -> result.append(discordService.shortInfo(user) + "\n"));
        return result.toString();
    }

    public void incrementCallsForUserByDiscordId(String discordId) {
        User user = findUserAndCreateIfNotFound(discordId);
        user.setNumberOfInteractions(user.getNumberOfInteractions() + 1);
        userRepository.save(user);
    }
}
