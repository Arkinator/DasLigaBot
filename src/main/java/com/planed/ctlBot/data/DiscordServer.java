package com.planed.ctlBot.data;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
public class DiscordServer {
    @Id
    @Column(nullable = false)
    private long serverId;
    @Column(nullable = false)
    private String announcerChannelId;
    @Column
    private String commandEscaper;
}
