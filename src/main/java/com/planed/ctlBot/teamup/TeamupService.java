package com.planed.ctlBot.teamup;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.planed.ctlBot.teamup.data.TeamupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamupService {
    Logger LOG = LoggerFactory.getLogger(TeamupService.class);

    @Autowired
    private TeamupClient teamupClient;
    @Value("${teamup.calendarId }")
    private String calendarId;

    public List<TeamupEvent> getEventsBetweenDates(LocalDate from, LocalDate to) {
        String rawResponse = teamupClient.getEventsBetweenDates(calendarId, from, to);
        LOG.info(rawResponse);
        JsonObject response = new JsonParser().parse(rawResponse).getAsJsonObject();
        List<TeamupEvent> result = new ArrayList<>();
        response.get("events").getAsJsonArray().forEach(jsonElement ->
                result.add(convertJsonElementToTeamupEvent(jsonElement.getAsJsonObject())));
        return result;
    }

    private TeamupEvent convertJsonElementToTeamupEvent(JsonObject jsonElement) {
        TeamupEvent result = new TeamupEvent();
        result.setTitle(jsonElement.get("title").getAsString());
        result.setWho(jsonElement.get("who").getAsString());
        result.setStart(convertDateTime(jsonElement.get("start_dt").getAsString()));
        result.setEnd(convertDateTime(jsonElement.get("end_dt").getAsString()));
        return result;
    }

    private OffsetDateTime convertDateTime(String value) {
        try {
            return OffsetDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atOffset(ZoneOffset.UTC);
        }
    }
}
