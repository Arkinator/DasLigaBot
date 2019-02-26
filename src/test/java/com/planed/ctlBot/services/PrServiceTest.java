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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PrServiceTest {
    private static final User PLAYER_A = aUserWithName("dfmkdfklö");
    private static final User PLAYER_B = aUserWithName("fjdiofm43qlfjw");

    @Mock
    DiscordService discordService;

    @InjectMocks
    PrService prService;

    @Before
    public void setUp() {
        when(discordService.getDiscordName(PLAYER_A)).thenReturn(PLAYER_A.getDiscordId());
        when(discordService.getDiscordName(PLAYER_B)).thenReturn(PLAYER_B.getDiscordId());
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
        return match;
    }

    private static User aUserWithName(String userName) {
        User result = new User();
        result.setDiscordId(userName);
        return result;
    }
}
