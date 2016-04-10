package com.planed.ctlBot.services;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.data.User;
import com.planed.ctlBot.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julian Peters on 10.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void giveUserAccessLevel(String userName, AccessLevel accessLevel) {
        User user = userRepository.findByDiscordId(userName);
        if (user == null){
            user = new User();
            user.setDiscordId(userName);
        }
        user.setAccessLevel(accessLevel);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);
        return result;
    }

    public String getNameForId(MessageReceivedEvent event, String discordId) {
        IUser user = event.getClient().getUserByID(getConvertedDiscordId(discordId));
        if (user != null)
            return user.getName();
        return "<>";
    }

    public String getConvertedDiscordId(String discordId) {
        if (discordId == null)
            return "";
        return discordId.substring(2,discordId.length()-1);
    }
}
