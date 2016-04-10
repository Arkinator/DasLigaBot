package com.planed.ctlBot.commands;

import com.planed.ctlBot.data.repositories.CtlMatchRepository;
import org.junit.Before;
import org.junit.Test;

import static com.planed.ctlBot.commands.BotCommandTestUtil.createMessageMock;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class AddMatchCommandTest {
    private CtlMatchRepository matchRepositoryMock;
    private AddMatchCommand addMatchCommand;

    @Before
    public void setUp() {
        matchRepositoryMock = mock(CtlMatchRepository.class);
        addMatchCommand = new AddMatchCommand(mock(BotCommandParser.class), matchRepositoryMock);
    }

    @Test
    public void addMatchWithTwoPlayers_ShouldBeAddedToDb() {
        addMatchCommand.execute(createMessageMock("!addMatch name1 name2"));
        verify(matchRepositoryMock, times(1)).save(argThat(new MatchEqualityMatcher("name1", "name2")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMatchWithOnePlayer_ExceptionShouldBeThrown(){
        addMatchCommand.execute(createMessageMock("!addMatch name1"));
    }

}
