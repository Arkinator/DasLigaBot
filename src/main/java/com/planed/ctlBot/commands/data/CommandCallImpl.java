package com.planed.ctlBot.commands.data;

import com.planed.ctlBot.domain.User;

import java.util.List;

/**
 * Created by Julian Peters on 17.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class CommandCallImpl implements CommandCall {
    private final String commandPhrase;
    private final User author;
    private final String channel;
    private final List<String> parameterList;
    private final List<User> mentionsList;

    CommandCallImpl(final String commandPhrase, final User author, final String channel, final List<String> parameterList, final List<User> mentionsList) {
        this.commandPhrase = commandPhrase;
        this.author = author;
        this.channel = channel;
        this.parameterList = parameterList;
        this.mentionsList = mentionsList;
    }

    @Override
    public User getAuthor() {
        return author;
    }

    @Override
    public String getCommandPhrase() {
        return commandPhrase;
    }

    @Override
    public List<String> getParameters() {
        return parameterList;
    }

    @Override
    public List<User> getMentions() {
        return mentionsList;
    }

    @Override
    public String getChannel() {
        return channel;
    }

    public List<User> getMentionsList() {
        return mentionsList;
    }

    public List<String> getParameterList() {
        return parameterList;
    }
}
