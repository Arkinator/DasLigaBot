package com.planed.ctlBot.commands;


import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.discord.DiscordCommand;
import com.planed.ctlBot.discord.DiscordController;
import com.planed.ctlBot.discord.DiscordService;
import com.planed.ctlBot.teamup.TeamupService;
import com.planed.ctlBot.teamup.data.TeamupEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@DiscordController
public class TeamupCommands {
	Logger LOG = LoggerFactory.getLogger(TeamupCommands.class);

	@Autowired
	private DiscordService discordService;
	@Autowired
	private TeamupService teamupService;

	@DiscordCommand(name = {"eventsNextWeek"}, help = "What is happening in the next week?")
	public void listEventsNextWeek(final CommandCall call) {
        List<TeamupEvent> eventList = teamupService.getEventsBetweenDates(LocalDate.now(), LocalDate.now().plusDays(7));
        StringBuilder builder = new StringBuilder();
        if (eventList.isEmpty()) {
            builder.append("No events in the next 7 days!");
        } else {
            builder.append("Events in the next 7 days:\n");
            eventList.forEach(event->convertEventToString(event, builder));
        }
        discordService.replyInChannel(call.getChannel(), builder.toString());
    }

    private void convertEventToString(TeamupEvent event, StringBuilder builder) {
	    builder.append(formatDateTime(event.getStart())+": '"+event.getTitle()+"'");
	    if (StringUtils.isEmpty(event.getWho())) {
            builder.append(event.getWho());
        }
        builder.append(" (");
        builder.append(outputDifferentLocaleDateTime(event.getStart(), "Angeles"));
        builder.append(outputDifferentLocaleDateTime(event.getStart(), "York"));
        builder.append(outputDifferentLocaleDateTime(event.getStart(), "Shanghai"));
        builder.append(outputDifferentLocaleDateTime(event.getStart(), "Berlin"));
        builder.append(")");
	}

    private String formatDateTime(OffsetDateTime dateTime) {
	    return dateTime.withOffsetSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm"))+" (UTC)";
    }

    private String outputDifferentLocaleDateTime(OffsetDateTime start, String zoneIdContains) {
        ZoneId zoneId = ZoneId.of(ZoneId.getAvailableZoneIds().stream().filter(id->id.contains(zoneIdContains)).findFirst().orElse("UTC"));
        if (!zoneId.getId().equals("UTC")){
            return start.atZoneSameInstant(zoneId).format(DateTimeFormatter.ofPattern("HH:mm")) + " (" + zoneId.getId() + "), ";
        } else {
            return "";
        }
    }
}
