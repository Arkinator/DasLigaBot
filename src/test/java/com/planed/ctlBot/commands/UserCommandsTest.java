package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.common.GameStatus;
import com.planed.ctlBot.common.Race;
import com.planed.ctlBot.data.repositories.MatchRepository;
import com.planed.ctlBot.data.repositories.UserRepository;
import com.planed.ctlBot.discord.CommandRegistry;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.testUtils.AbstractDiscordCommandTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;

import static com.planed.ctlBot.domain.UserFixtures.aDefaultUser;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(SpringRunner.class)
public class UserCommandsTest extends AbstractDiscordCommandTest {
    @Autowired
    private CommandRegistry commandRegistry;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MatchRepository matchRepository;
    private User user1;
    private User user2;

    @Before
    public void setUp() {
        if (user1 != null) {
            return;
        }

        user1 = newUser(aDefaultUser());
        user2 = newUser(aDefaultUser());

        registerUser(user1);
        registerUser(user2);
    }

    @Test
    public void shouldChangeUsersRace() {
        final Race oldRace = user1.getRace();

        commandRegistry.fireEvent(aChangeRaceEvent(user1));

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).get().getRace(), is(not(oldRace)));
    }

    @Test
    public void issueChallengeShouldCreateNewMatch() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));

        Long matchId = userRepository.findByDiscordId(user1.getDiscordId()).get().getMatchId();
        assertThat(matchRepository.findById(matchId), is(not(nullValue())));
        assertThat(matchRepository.findById(matchId), is(not(nullValue())));
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

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).get().getMatchId(), is(nullValue()));
        assertThat(userRepository.findByDiscordId(user2.getDiscordId()).get().getMatchId(), is(nullValue()));
    }

    @Test
    public void theAuthorCanNotRejectAChallengeHeMade() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "reject"));

        assertThat(matchRepository.findMatchByPlayerA(user1.getDiscordId()), is(not(nullValue())));
    }

    @Test
    public void theAuthorCanNotAcceptAChallengeHeMade() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "accept"));

        assertThat(matchRepository.findMatchByPlayerA(user1.getDiscordId()).get().getGameStatus(), is(GameStatus.CHALLENGE_EXTENDED));
    }

    @Test
    public void theAuthorCanRevokeAChallengeHeMade() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "revoke"));

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).get().getMatchId(), is(nullValue()));
    }

    @Test
    public void theChallengeCanNotRevokeAChallengeMade() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "revoke"));

        assertThat(matchRepository.findMatchByPlayerA(user1.getDiscordId()).get(), is(not(nullValue())));
    }

    @Test
    public void anAcceptedChallengeShouldGiveAnActiveMatch() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));

        user2 = userRepository.findByDiscordId(user2.getDiscordId()).get();
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        user1 = userRepository.findByDiscordId(user1.getDiscordId()).get();

        assertThat(matchRepository.findById(user1.getMatchId()).get().getGameStatus(),
                is(GameStatus.CHALLENGE_ACCEPTED));
    }

    @Test
    public void aChallengerCanNotAcceptedHisOwnChallenge() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "accept"));

        assertThat(matchRepository.findMatchByPlayerA(user1.getDiscordId()).get().getGameStatus(),
                is(GameStatus.CHALLENGE_EXTENDED));
    }

    @Test
    public void acceptedGameWithReportGivesPartialReported() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));

        assertThat(matchRepository.findById(user1.getMatchId()).get().getGameStatus(), is(GameStatus.PARTIALLY_REPORTED));
    }

    @Test
    public void bothReportsGiveFinishedGame() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        assertThat(matchRepository.findById(user1.getMatchId()).get().getGameStatus(), is(GameStatus.GAME_PLAYED));
    }

    @Test
    public void finishedGameHasCorrectScore() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        assertThat(matchRepository.findById(user1.getMatchId()).get().getFinalScorePlayerA(), is(1));
        assertThat(matchRepository.findById(user1.getMatchId()).get().getFinalScorePlayerB(), is(0));
    }

    @Test
    public void differingReportsGiveConflictingGame() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "win"));

        assertThat(matchRepository.findById(user1.getMatchId()).get().getGameStatus(), is(GameStatus.CONFLICT_STATE));
    }

    @Test
    public void userCanChangeResultsForConflictingGames() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).get().getMatchId(), is(nullValue()));
    }

    @Test
    public void userCanChangeResultsForPartialReportedGame() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "loss"));
        final Long matchId = user1.getMatchId();
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "win"));

        assertThat(matchRepository.findById(matchId).get().getGameStatus(), is(GameStatus.GAME_PLAYED));
        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).get().getMatchId(), is(nullValue()));
    }

    @Test
    public void eloIncreasesForWinner() {
        final double eloBefore = user1.getElo();
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        assertThat(userRepository.findByDiscordId(user1.getDiscordId()).get().getElo(), is(greaterThan(eloBefore)));
    }

    @Test
    public void eloWinsAndLossesAreEqual() {
        final double elo1Before = user1.getElo();
        final double elo2Before = user2.getElo();
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));
        commandRegistry.fireEvent(aSimpleCommand(user2, "report", "loss"));

        final double change1 = elo1Before - userRepository.findByDiscordId(user1.getDiscordId()).get().getElo();
        final double change2 = -(elo2Before - userRepository.findByDiscordId(user2.getDiscordId()).get().getElo());
        assertThat(change1, is(change2));
    }

    @Test
    public void canNotReportOnAnUnacceptedChallenge() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "report", "win"));

        assertThat(matchRepository.findMatchByPlayerA(user1.getDiscordId()).get().getGameStatus(),
                is(GameStatus.CHALLENGE_EXTENDED));
    }

    @Test
    public void challengerCancels_shouldRevokeMatch() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user1, "cancel"));

        assertThat(matchRepository.findMatchByPlayerA(user1.getDiscordId()).get().getGameStatus(), is(GameStatus.CHALLENGE_REVOKED));
    }

    @Test
    public void cancelOnAcceptedMatch_shouldNotChangeState() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "accept"));
        commandRegistry.fireEvent(aSimpleCommand(user1, "cancel"));

        assertThat(matchRepository.findMatchByPlayerA(user1.getDiscordId()).get().getGameStatus(), is(GameStatus.CHALLENGE_ACCEPTED));
    }

    @Test
    public void challengeeCancels_shouldRevokeMatch() {
        commandRegistry.fireEvent(anIssueChallengeCommand(user1, user2));
        commandRegistry.fireEvent(aSimpleCommand(user2, "cancel"));

        assertThat(matchRepository.findMatchByPlayerA(user1.getDiscordId()).get().getGameStatus(), is(GameStatus.CHALLENGE_REJECTED));
    }

    private DiscordMessage aSimpleCommand(final User user, final String command, final String... parameters) {
        return DiscordMessage.builder()
                .author(user)
                .commandPhrase(command)
                .channel(1234l)
                .parameters(Arrays.asList(parameters))
                .mentions(Collections.emptyList())
                .build();
    }

    private User newUser(final User user) {
        return userRepository.save(user);
    }

    private DiscordMessage anIssueChallengeCommand(final User user1, final User user2) {
        return DiscordMessage.builder()
                .author(user1)
                .commandPhrase("challenge")
                .mentions(Collections.singletonList(user2))
                .channel(1234l)
                .serverId(12l)
                .build();
    }

    private DiscordMessage aChangeRaceEvent(final User user) {
        return DiscordMessage.builder()
                .author(user)
                .commandPhrase("changeRace")
                .parameters(Collections.singletonList("Protoss"))
                .channel(1234l)
                .mentions(Collections.emptyList())
                .serverId(12l)
                .build();
    }
}
