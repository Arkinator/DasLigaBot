package com.planed.ctlBot.commands.data;

import com.planed.ctlBot.domain.User;
import lombok.Builder;
import lombok.Data;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class DiscordMessage {
    private final User author;
    private final String channel;
    private final String serverId;
    private final String messageId;
    private final TextChannel textChannel;
    private final Message message;
    private final org.javacord.api.entity.user.User discordUser;
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
