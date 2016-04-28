package com.planed.ctlBot.commands.data;

import com.planed.ctlBot.domain.User;

import java.util.List;

public interface CommandCall {
    /**
     * Returns the Discord-ID of the Author of the command
     * @return The Id in plain form, without braces and add-sign
     */
    User getAuthor();

    String getCommandPhrase();

    List<String> getParameters();

    List<User> getMentions();

    String getChannel();
}
