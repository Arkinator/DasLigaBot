package com.planed.ctlBot.services;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.common.Race;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.Match;
import com.planed.ctlBot.domain.MatchRepository;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
        if (user == null){
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

    public User findUserAndCreateIfNotFound(final String discordId){
        User result =  userRepository.findByDiscordId(discordId);
        if (result == null){
            addNewUser(discordId);
            result =  userRepository.findByDiscordId(discordId);
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
        Assert.isTrue(author.getMatch() == null);
        Assert.isTrue(challengee.getMatch() == null);
        matchRepository.addMatch(author, challengee);
    }

    public void rejectChallenge(final User author) {
        // the author needs to have an assigned match
        Assert.isTrue(author.getMatch() != null);
        final Match match = author.getMatch();
        // you can only reject challenges you did not make
        Assert.isTrue(author.getUserId() != author.getMatch().getPlayers().get(0).getUserId());
        match.setGameStatus(GameStatus.challengeRejected);
        matchRepository.saveMatch(match);
        match.getPlayers().forEach(p->{
                p.setMatch(null);
                userRepository.save(p);
        });
    }
}
