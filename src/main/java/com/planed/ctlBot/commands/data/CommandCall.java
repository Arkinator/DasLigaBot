package com.planed.ctlBot.commands.data;

import com.planed.ctlBot.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommandCall {
    private final String commandPhrase;
    private final User author;
    private final String channel;
    private final List<String> parameters;
    private final List<User> mentions;
    private final String serverId;
}
