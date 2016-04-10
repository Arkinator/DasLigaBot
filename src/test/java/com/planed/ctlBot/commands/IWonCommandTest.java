package com.planed.ctlBot.commands;

import com.planed.ctlBot.data.CtlMatch;
import com.planed.ctlBot.services.CtlMatchService;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.planed.ctlBot.commands.BotCommandTestUtil.buildMentionTag;
import static com.planed.ctlBot.commands.BotCommandTestUtil.createMessageMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class IWonCommandTest {
    private static final String AUTHOR_ID = "authorIdInOneWord";
    private static final String OTHER_ID = "anotherIdInOneWord";
    private IWonCommand iWonCommand;
    private CtlMatchService matchServiceMock;
    private CtlMatch matchMock;

    @Before
    public void setUp() throws Exception {
        matchServiceMock = mock(CtlMatchService.class);
        iWonCommand = new IWonCommand(matchServiceMock, mock(BotCommandParser.class));
        matchMock = mock(CtlMatch.class);
        when(matchMock.getPlayer1()).thenReturn(buildMentionTag(AUTHOR_ID));
        when(matchMock.getPlayer2()).thenReturn(buildMentionTag(OTHER_ID));
    }

    @Test
    public void callWithPlayer1Matching_shouldSaveWinner() throws Exception {
        when(matchServiceMock.findMatchInCurrentWeekForUser(buildMentionTag(AUTHOR_ID))).thenReturn(Optional.of(matchMock));
        iWonCommand.execute(createMessageMock("!iwon", AUTHOR_ID));
        verify(matchServiceMock, times(1)).saveMatch(matchMock);
        verify(matchMock).setWinner(buildMentionTag(AUTHOR_ID));
    }

    @Test
    public void callWithPlayer2Matching_shouldSaveWinner() throws Exception {
        when(matchServiceMock.findMatchInCurrentWeekForUser(buildMentionTag(AUTHOR_ID))).thenReturn(Optional.of(matchMock));
        iWonCommand.execute(createMessageMock("!iwon", AUTHOR_ID));
        verify(matchServiceMock, times(1)).saveMatch(matchMock);
        verify(matchMock).setWinner(buildMentionTag(AUTHOR_ID));
    }

    @Test
    public void callOnAlreadyDecidedMatch_shouldSaveNewWinner() throws Exception {
        when(matchServiceMock.findMatchInCurrentWeekForUser(buildMentionTag(OTHER_ID))).thenReturn(Optional.of(matchMock));
        when(matchMock.getWinner()).thenReturn(buildMentionTag(AUTHOR_ID));
        iWonCommand.execute(createMessageMock("!iwon", OTHER_ID));
        verify(matchServiceMock, times(1)).saveMatch(matchMock);
        verify(matchMock).setWinner(buildMentionTag(OTHER_ID));
    }

    @Test
    public void callWithNoPlayerMatching_shouldNotSaveAnything()throws Exception {
        when(matchServiceMock.findMatchInCurrentWeekForUser(buildMentionTag(AUTHOR_ID))).thenReturn(Optional.empty());
        iWonCommand.execute(createMessageMock("!iwon", AUTHOR_ID));
        verify(matchServiceMock, times(0)).saveMatch(matchMock);
    }
}
