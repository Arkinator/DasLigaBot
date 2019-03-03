package com.planed.ctlBot.services;

import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.Match;
import com.planed.ctlBot.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrServiceTest {
    private static final User PLAYER_A = aUserWithName("dfmkdfklö");
    private static final User PLAYER_B = aUserWithName("fjdiofm43qlfjw");

    @Mock
    private DiscordService discordService;
    @Mock
    private ServerService serverService;
    @InjectMocks
    private PrService prService;

    @Before
    public void setUp() {
        when(discordService.getDiscordName(eq(PLAYER_A.getDiscordId()), any())).thenReturn(PLAYER_A.getDiscordId());
        when(discordService.getDiscordName(eq(PLAYER_B.getDiscordId()), any())).thenReturn(PLAYER_B.getDiscordId());

        when(serverService.findAnnouncerChannelIdForServer(any())).thenReturn(Optional.of(123l));
    }

    @Test
    public void matchResultMessageShouldContainBothContenderNames(){
        prService.printMessageResultMessage(aMatchResultMessage());

        assertThat(getLastDiscordCall(), containsString(PLAYER_A.getDiscordId()));
    }

    @Test
    public void challengeExtendedMessageShouldContainBothContenderNames(){
        prService.printChallengeExtendedMessage(aMatchResultMessage());

        assertThat(getLastDiscordCall(), containsString(PLAYER_A.getDiscordId()));
    }

    @Test
    public void gameIsOnMessageShouldContainBothContenderNames(){
        prService.printGameIsOnMessage(aMatchResultMessage());

        assertThat(getLastDiscordCall(), containsString(PLAYER_A.getDiscordId()));
    }

    private String getLastDiscordCall() {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(discordService).replyInChannel(any(), any(), argument.capture());
        return argument.getValue();
    }

    private Match aMatchResultMessage() {
        Match match = new Match();
        match.setFinalScorePlayerA(0);
        match.setFinalScorePlayerB(2);
        match.setPlayers(Arrays.asList(PLAYER_A, PLAYER_B));
        match.setOriginatingChannelId(123l);
        match.setOriginatingServerId(123l);
        return match;
    }

    private static User aUserWithName(String userName) {
        User result = new User();
        result.setDiscordId(userName);
        return result;
    }
}
