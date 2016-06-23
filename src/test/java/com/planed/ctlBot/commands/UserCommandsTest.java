package com.planed.ctlBot.commands;

import com.planed.ctlBot.BotBoot;
import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.commands.data.CommandCallBuilder;
import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.common.Race;
import com.planed.ctlBot.data.repositories.UserEntityRepository;
import com.planed.ctlBot.discord.CommandRegistry;
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

import java.util.Arrays;
import java.util.Collections;

import static com.planed.ctlBot.domain.UserFixtures.aDefaultUser;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

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
    private UserEntityRepository userEntityRepository;
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
        final Long matchId = user1.getMatchId();
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        assertThat(matchId, is(user1.getMatchId()));
    }

    @Test
    public void challengeRejectedShouldFreeBothPlayers() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "reject"));

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).getMatchId(), is(nullValue()));
        assertThat(userRepository.findByDiscordId(user2.getDiscordId()).getMatchId(), is(nullValue()));
    }

    @Test
    public void theAuthorCanNotRejectAChallengeHeMade() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "reject"));

        assertThat(matchRepository.findMatchForUser(user1.getDiscordId()), is(not(nullValue())));
    }

    @Test
    public void theAuthorCanNotAcceptAChallengeHeMade() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "accept"));

        assertThat(matchRepository.findMatchForUser(user1.getDiscordId()).getGameStatus(), is(GameStatus.challengeExtended));
    }

    @Test
    public void theAuthorCanRevokeAChallengeHeMade() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "revoke"));

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).getMatchId(), is(nullValue()));
    }

    @Test
    public void theChallengeCanNotRevokeAChallengeMade() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "revoke"));

        assertThat(matchRepository.findMatchForUser(user1.getDiscordId()), is(not(nullValue())));
    }

    @Test
    public void anAcceptedChallengeShouldGiveAnActiveMatch() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));

        user2 = userRepository.findByDiscordId(user2.getDiscordId());
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        user1 = userRepository.findByDiscordId(user1.getDiscordId());

        assertThat(matchRepository.findMatchById(user1.getMatchId()).getGameStatus(),
                is(GameStatus.challengeAccepted));
    }

    @Test
    public void aChallengerCanNotAcceptedHisOwnChallenge() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "accept"));

        assertThat(matchRepository.findMatchForUser(user1.getDiscordId()).getGameStatus(),
                is(GameStatus.challengeExtended));
    }

    @Test
    public void acceptedGameWithReportGivesPartialReported() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));

        assertThat(matchRepository.findMatchById(user1.getMatchId()).getGameStatus(), is(GameStatus.partiallyReported));
    }

    @Test
    public void bothReportsGiveFinishedGame() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        assertThat(matchRepository.findMatchById(user1.getMatchId()).getGameStatus(), is(GameStatus.gamePlayed));
    }

    @Test
    public void finishedGameHasCorrectScore() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        assertThat(matchRepository.findMatchById(user1.getMatchId()).getFinalScorePlayerA(), is(1));
        assertThat(matchRepository.findMatchById(user1.getMatchId()).getFinalScorePlayerB(), is(0));
    }

    @Test
    public void differingReportsGiveConflictingGame() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "win"));

        assertThat(matchRepository.findMatchById(user1.getMatchId()).getGameStatus(), is(GameStatus.conflictState));
    }

    @Test
    public void userCanChangeResultsForConflictingGames() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).getMatchId(), is(nullValue()));
    }

    @Test
    public void userCanChangeResultsForPartialReportedGame() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "loss"));
        final Long matchId = user1.getMatchId();
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "win"));

        assertThat(matchRepository.findMatchById(matchId).getGameStatus(), is(GameStatus.gamePlayed));
        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).getMatchId(), is(nullValue()));
    }

    @Test
    public void eloIncreasesForWinner() {
        final double eloBefore = user1.getElo();
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).getElo(), is(greaterThan(eloBefore)));
    }

    @Test
    public void eloWinsAndLossesAreEqual() {
        final double elo1Before = user1.getElo();
        final double elo2Before = user2.getElo();
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        final double change1 = elo1Before - userRepository.findByDiscordId(user1.getDiscordId()).getElo();
        final double change2 = - (elo2Before - userRepository.findByDiscordId(user2.getDiscordId()).getElo());
        assertThat(change1, is(change2));
    }

    @Test
    public void canNotReportOnAnUnacceptedChallenge() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));

        assertThat(matchRepository.findMatchForUser(user1.getDiscordId()).getGameStatus(),
                is(GameStatus.challengeExtended));
    }

    private CommandCall aSimpleCommand(final User user, final String command, final String... parameters) {
        return new CommandCallBuilder()
                .setAuthor(user)
                .setCommandPhrase(command)
                .setChannel("fjkdsl")
                .setParameterList(Arrays.asList(parameters))
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
