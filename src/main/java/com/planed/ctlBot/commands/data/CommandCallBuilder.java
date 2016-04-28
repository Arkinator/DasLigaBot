package com.planed.ctlBot.commands.data;

import com.planed.ctlBot.domain.User;

import java.util.List;

public class CommandCallBuilder {
    private String commandPhrase;
    private User author;
    private String channel;
    private List<String> parameterList;
    private List<User> mentionsList;

    public CommandCallBuilder setCommandPhrase(final String commandPhrase) {
        this.commandPhrase = commandPhrase;
        return this;
    }

    public CommandCallBuilder setAuthor(final User author) {
        this.author = author;
        return this;
    }

    public CommandCallBuilder setChannel(final String channel) {
        this.channel = channel;
        return this;
    }

    public CommandCallBuilder setParameterList(final List<String> parameterList) {
        this.parameterList = parameterList;
        return this;
    }

    public CommandCallBuilder setMentionsList(final List<User> mentionsList) {
        this.mentionsList = mentionsList;
        return this;
    }

    public CommandCall createCommandCall() {
        return new CommandCallImpl(commandPhrase, author, channel, parameterList, mentionsList);
    }
}
