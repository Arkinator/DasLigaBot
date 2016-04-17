package com.planed.ctlBot.commands.data;

import java.util.List;

/**
 * Created by Julian Peters on 17.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public interface CommandCall {
    /**
     * Returns the Discord-ID of the Author of the command
     * @return The Id in plain form, without braces and add-sign
     */
    String getAuthorId();

    String getCommandPhrase();

    List<String> getParameters();

    List<String> getMentions();
}
