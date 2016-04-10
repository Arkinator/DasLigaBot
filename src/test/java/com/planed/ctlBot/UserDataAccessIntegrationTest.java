package com.planed.ctlBot;

import com.planed.ctlBot.data.User;
import com.planed.ctlBot.data.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.planed.ctlBot.common.AccessLevel.User;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(BotBoot.class)
public class UserDataAccessIntegrationTest {
    private static final String USER_DISCORD_ID = "some random id";

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.delete(userRepository.findAll());
    }

    @Test
    public void createNewUser_itShouldBeFound_deleteAfter() {
        User dbUser = userRepository.findByDiscordId(USER_DISCORD_ID);
        assertThat(dbUser, nullValue());

        User user = new User();
        user.setDiscordId(USER_DISCORD_ID);
        userRepository.save(user);

        dbUser = userRepository.findByDiscordId(USER_DISCORD_ID);
        assertThat(dbUser, not(nullValue()));
        assertThat(dbUser.getDiscordId(), equalTo(USER_DISCORD_ID));

        userRepository.delete(dbUser);

        dbUser = userRepository.findByDiscordId(USER_DISCORD_ID);
        assertThat(dbUser, nullValue());
    }
}
