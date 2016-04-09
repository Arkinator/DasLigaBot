package com.planed.ctlBot;

import com.planed.ctlBot.data.CtlMatch;
import org.junit.Before;
import org.junit.Test;
import sx.blah.discord.handle.obj.IUser;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class CtlDataStoreTest {
    private CtlDataStore dataStore;

    @Before
    public void setUp(){
        dataStore = new CtlDataStore();
    }

    @Test
    public void addMatch_shouldBePresentAfter() throws Exception {
        IUser user1 = mock(IUser.class);
        IUser user2 = mock(IUser.class);
        dataStore.addMatch(user1, user2);
        assertThat(dataStore.getMatches().size(), is(1));
    }

    @Test
    public void addMatch_shouldHaveCorrectPlayers() throws Exception {
        IUser user1 = mock(IUser.class);
        IUser user2 = mock(IUser.class);
        dataStore.addMatch(user1, user2);
        CtlMatch match = dataStore.getMatches().get(0);
        assertThat(match.getPlayers(), hasItems(user1, user2));
    }
}
