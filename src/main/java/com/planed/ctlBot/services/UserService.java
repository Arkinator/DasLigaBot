package com.planed.ctlBot.services;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.GameResult;
import com.planed.ctlBot.common.GameStatus;
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

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class UserService {
    private final DiscordService discordService;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final DiscordService discordService,
                       final MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.discordService = discordService;
        this.matchRepository = matchRepository;
    }

    public void giveUserAccessLevel(final String userName, final AccessLevel accessLevel) {
        User user = userRepository.findByDiscordId(userName);
        if (user == null) {
            user = new User();
            user.setDiscordId(userName);
        }
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
        discordService.createNewUserFromId(discordId);
    }

    public void changeRace(final User author, final String newRace) {
        author.setRace(Race.valueOf(newRace));
        userRepository.save(author);
    }

    public void issueChallenge(final User author, final User challengee) {
        matchRepository.addMatch(author, challengee);
        discordService.whisperToUser(challengee.getDiscordId(),
                "You have been challenged by "
                        + discordService.shortInfo(author)
                        + ". You can !reject or !accept the challenge");
    }

    public void rejectChallenge(final User author) {
        final Match match = author.getMatch();
        match.setGameStatus(GameStatus.challengeRejected);
        matchRepository.saveMatch(match);
        clearMatchOfInvolvedPlayers(match);
    }

    public void revokeChallenge(final User author) {
        final Match match = author.getMatch();
        match.setGameStatus(GameStatus.challengeRevoked);
        matchRepository.saveMatch(match);
        clearMatchOfInvolvedPlayers(match);
    }

    public void acceptChallenge(final User author) {
        final Match match = author.getMatch();
        match.setGameStatus(GameStatus.challengeAccepted);
        matchRepository.saveMatch(match);
        updateMatch(match);
    }

    public void reportResult(final Match match, final User author, final GameResult result) {
        if (!match.didUserReportResult(author)) {
            match.reportResult(author, result);
            if (match.getGameStatus() == GameStatus.gamePlayed) {
                finalizeGame(match);
            }
            matchRepository.saveMatch(match);
        }
    }

    private void finalizeGame(final Match match) {
        if (match.getGameStatus() == GameStatus.gamePlayed) {
            clearMatchOfInvolvedPlayers(match);
        }
    }

    private void clearMatchOfInvolvedPlayers(final Match match) {
        match.getPlayers().forEach(p -> {
            p.setMatch(null);
            userRepository.save(p);
        });
    }

    private void updateMatch(final Match match) {
//        match.getPlayers().forEach(p -> {
//            p.setMatch(match);
//            userRepository.save(p);
//        });
    }
}
