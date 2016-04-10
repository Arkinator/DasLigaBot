package com.planed.ctlBot;

import com.planed.ctlBot.data.CtlMatch;
import com.planed.ctlBot.data.User;
import com.planed.ctlBot.data.repositories.CtlMatchRepository;
import com.planed.ctlBot.data.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(BotBoot.class)
public class CtlMatchDataAccessIntegrationTest {
    private static final String USER_DISCORD_ID_1 = "some random id";
    private static final String USER_DISCORD_ID_2 = "another random id";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CtlMatchRepository ctlMatchRepository;

    private User user1;
    private User user2;

    @Before
    public void setUp() {
        userRepository.delete(userRepository.findAll());

        user1 = new User();
        user1.setDiscordId(USER_DISCORD_ID_1);
        userRepository.save(user1);

        user2 = new User();
        user2.setDiscordId(USER_DISCORD_ID_2);
        userRepository.save(user2);
    }

    @Test
    public void createNewMatch_itShouldBeFound_deleteAfter() {
        assertThat(ctlMatchRepository.count(), equalTo(0l));

        CtlMatch match = new CtlMatch(USER_DISCORD_ID_1, USER_DISCORD_ID_2);
        ctlMatchRepository.save(match);

        assertThat(ctlMatchRepository.count(), equalTo(1l));

        ctlMatchRepository.delete(match);

        assertThat(ctlMatchRepository.count(), equalTo(0l));
    }

    @Test
    public void createNewMatch_AccessByUser1Id_deleteAfter() {
        CtlMatch match = new CtlMatch(USER_DISCORD_ID_1, USER_DISCORD_ID_2);
        ctlMatchRepository.save(match);

        assertThat(ctlMatchRepository.findMatchByPlayer1(USER_DISCORD_ID_1).size(), equalTo(1));

        ctlMatchRepository.delete(match);
    }

    @Test
    public void createNewMatch_AccessByUser2Id_deleteAfter() {
        CtlMatch match = new CtlMatch(USER_DISCORD_ID_1, USER_DISCORD_ID_2);
        ctlMatchRepository.save(match);

        assertThat(ctlMatchRepository.findMatchByPlayer2(USER_DISCORD_ID_2).size(), equalTo(1));

        ctlMatchRepository.delete(match);
    }
}
