package com.planed.ctlBot.services;

import com.planed.ctlBot.commands.data.CommandCall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julian Peters on 17.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class CommandCallImpl implements CommandCall {
    private String commandPhrase;
    private String authorId;
    private List<String> parameterList = new ArrayList<>();;
    private List<String> mentionsList = new ArrayList<>();

    @Override
    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(final String authorId) {
        this.authorId = authorId;
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
    public List<String> getMentions() {
        return mentionsList;
    }

    public void addMention(String userId) {
        mentionsList.add(userId);
    }

    public void addParameter(String parameter) {
        parameterList.add(parameter);
    }

    public void setCommandPhrase(final String commandPhrase){
        this.commandPhrase = commandPhrase;
    }
}
