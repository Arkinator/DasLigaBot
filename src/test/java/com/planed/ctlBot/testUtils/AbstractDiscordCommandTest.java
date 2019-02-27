package com.planed.ctlBot.testUtils;

import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.User;
import org.javacord.api.DiscordApi;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AbstractDiscordCommandTest {
    @MockBean
    private DiscordApi discordApi;
    @MockBean
    private DiscordService discordService;
    private List<User> knownUsers = new ArrayList<>();

    @Before
    public void setUpMocks() {
        when(discordService.getDiscordName(any())).then(call -> ((User)call.getArguments()[0]).getDiscordId());
    }

    public void registerUser(User user) {
        knownUsers.add(user);
    }
}
