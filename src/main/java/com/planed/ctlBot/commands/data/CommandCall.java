package com.planed.ctlBot.commands.data;

import com.planed.ctlBot.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class CommandCall {
    private final String commandPhrase;
    private final User author;
    private final String channel;
    @Builder.Default
    private final List<String> parameters = Collections.emptyList();
    @Builder.Default
    private final List<User> mentions = Collections.emptyList();
    private final String serverId;
}
