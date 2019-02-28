package com.planed.ctlBot.commands.data;

import com.planed.ctlBot.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class DiscordMessage {
    private final User author;
    private final String channel;
    private final String serverId;
    private final String messageId;
    @Builder.Default
    private final List<User> mentions = Collections.emptyList();
    private final String commandPhrase;
    @Builder.Default
    private List<String> parameters = Collections.emptyList();

    public boolean isBotCommand() {
        return commandPhrase != null;
    }

    public boolean isPrivateMessage() {
        return serverId == null;
    }
}
