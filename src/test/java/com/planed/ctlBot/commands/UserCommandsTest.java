package com.planed.ctlBot.commands;

import com.planed.ctlBot.BotBoot;
import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.commands.data.CommandCallBuilder;
import com.planed.ctlBot.common.Race;
import com.planed.ctlBot.discord.CommandRegistry;
import com.planed.ctlBot.domain.Match;
import com.planed.ctlBot.domain.MatchRepository;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.domain.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.planed.ctlBot.domain.UserFixtures.aDefaultUser;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringApplicationConfiguration(classes = {
        BotBoot.class
})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("development")
@Transactional
public class UserCommandsTest {
    @Autowired
    private CommandRegistry commandRegistry;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private MatchRepository matchEntityRepository;
    private User user1;
    private User user2;

    @Before
    public void setUp() {
        user1 = newUser(aDefaultUser());
        user2 = newUser(aDefaultUser());
    }

    @Test
    public void shouldChangeUsersRace() {
        final Race oldRace = user1.getRace();

        commandRegistry.fireEvent(aChangeRaceEvent(user1));

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).getRace(), is(not(oldRace)));
    }

    @Test
    public void issueChallengeShouldCreateNewMatch() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));

        assertThat(matchRepository.findMatchForUser(user1.getDiscordId()), is(not(nullValue())));
        assertThat(matchRepository.findMatchForUser(user2.getDiscordId()), is(not(nullValue())));
    }

    @Test
    public void issueChallengeShouldNotBePossibleIfPlayerAlreadyHasAGame() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        final Match match = user1.getMatch();
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        assertThat(match.getMatchId(), is(user1.getMatch().getMatchId()));
    }

    @Test
    public void challengeRejectedShouldFreeBothPlayers() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aRejectChallengeCommand(user2));

        assertThat(matchRepository.findMatchForUser(user1.getDiscordId()), is(nullValue()));
        assertThat(matchRepository.findMatchForUser(user2.getDiscordId()), is(nullValue()));
    }

    @Test
    public void theAuthorCanNotRejectAChallengeHeMade() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aRejectChallengeCommand(user1));

        assertThat(matchRepository.findMatchForUser(user1.getDiscordId()), is(not(nullValue())));
    }

    private CommandCall aRejectChallengeCommand(final User user) {
        return new CommandCallBuilder()
                .setAuthor(user)
                .setCommandPhrase("reject")
                .setChannel("fjkdsl")
                .createCommandCall();
    }

    private User newUser(final User user) {
        userRepository.save(user);
        return userRepository.findByDiscordId(user.getDiscordId());
    }

    private CommandCall anIssueChallengeCommand(final User user1, final User user2) {
        return new CommandCallBuilder()
                .setAuthor(user1)
                .setCommandPhrase("challenge")
                .setMentionsList(Collections.singletonList(user2))
                .setChannel("fjkdsl")
                .createCommandCall();
    }

    private CommandCall aChangeRaceEvent(final User user) {
        return new CommandCallBuilder()
                .setAuthor(user)
                .setCommandPhrase("changeRace")
                .setParameterList(Collections.singletonList("Protoss"))
                .setChannel("fjkdsl")
                .createCommandCall();
    }
}
