package com.planed.ctlBot.services;

import com.planed.ctlBot.data.repositories.UserRepository;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {
    @Mock
    DiscordService discordService;
    @Mock
    UserRepository userRepository;
    @Mock
    User mockUser;
    @Mock
    User anotherMockUser;

    @InjectMocks
    UserService userService;
    private String mockDiscordId;
    private String anotherMockDiscordId;

    @Before
    public void setUp() {
        mockDiscordId = RandomStringUtils.randomAlphanumeric(20);
        anotherMockDiscordId = RandomStringUtils.randomAlphanumeric(20);
        initMocks(this);

        when(userRepository.findByDiscordId(eq(mockDiscordId))).thenReturn(Optional.of(mockUser));
        when(mockUser.getDiscordId()).thenReturn(mockDiscordId);

        when(anotherMockUser.getDiscordId()).thenReturn(anotherMockDiscordId);

//        when(discordService.createNewUserFromId(eq(anotherMockDiscordId))).thenReturn(anotherMockUser);
    }

    @Test
    public void findExistingUser_ShouldReturnMatchingUser(){
        assertThat(userService.findUserAndCreateIfNotFound(mockDiscordId), equalTo(mockUser));
        assertThat(userService.findUserAndCreateIfNotFound(mockDiscordId).getDiscordId(), equalTo(mockDiscordId));
    }

    @Test
    public void findNonExistentUser_ShouldReturnMatchingUser(){
        when(userRepository.findByDiscordId(eq(anotherMockDiscordId))).thenReturn(Optional.empty(), Optional.of(anotherMockUser));

        assertThat(userService.findUserAndCreateIfNotFound(anotherMockDiscordId).getDiscordId(), equalTo(anotherMockDiscordId));
        verify(userRepository, times(2)).findByDiscordId(eq(anotherMockDiscordId));
    }
}
