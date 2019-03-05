package com.planed.ctlBot.data;

import com.planed.ctlBot.common.League;
import com.planed.ctlBot.common.Race;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BattleNetInformation {
    private String playerName;
    private Long mmr;
    private Race race;
    private League league;
}
