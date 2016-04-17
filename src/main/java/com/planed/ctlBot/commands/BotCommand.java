package com.planed.ctlBot.commands;

import com.planed.ctlBot.commands.data.CommandCall;

/**
 * Created by Julian Peters on 17.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public interface BotCommand {
    void messageReceiver(CommandCall command);
}
