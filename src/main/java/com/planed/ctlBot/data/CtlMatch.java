package com.planed.ctlBot.data;

import sx.blah.discord.handle.obj.IUser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class CtlMatch {
    private List<IUser> players;

    public CtlMatch(IUser... users) {
        players = Arrays.asList(users);
    }

    public List<IUser> getPlayers() {
        return players;
    }
}
