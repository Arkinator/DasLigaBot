package com.planed.ctlBot.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscordServer {
    @Id
    @Column(nullable = false)
    private long serverId;
    @Column(nullable = false)
    private long announcerChannelId;
    @Column
    private String commandEscaper;
}
