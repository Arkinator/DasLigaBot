package com.planed.ctlBot.teamup;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TeamupClient {
    Logger LOG = LoggerFactory.getLogger(TeamupClient.class);

    private final static String BASE_URL = "https://api.teamup.com/";
    @Value("${teamup.apiKey}")
    private String apiKey;

    private TeamupClient() {

    }

    public static TeamupClient build(String apiKey) {
        TeamupClient result = new TeamupClient();
        result.setApiKey(apiKey);
        return result;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean checkConnection() {
        try {
            HttpResponse<JsonNode> response = Unirest.get(BASE_URL + "check-access")
                    .header("Teamup-Token", apiKey)
                    .asJson();
            if (response.getStatus() == 200
                    && response.getBody().getObject().has("access")
                    && response.getBody().getObject().get("access").equals("ok")){
                return true;
            } else {
                return false;
            }
        } catch (UnirestException e) {
            LOG.info("Failed to authenticate connection to Teamup: ", e);
            return false;
        }
    }

    public String getEventsBetweenDates(String calendarId, LocalDate from, LocalDate to) {
        try {
            HttpResponse<String> response = Unirest.get(BASE_URL + calendarId + "/events"
            +"?startDate="+getTeamupDate(from)+"&endDate="+getTeamupDate(to))
                    .header("Teamup-Token", apiKey)
                    .asString();
            return response.getBody();
        } catch (UnirestException e) {
            throw new TeamupConnectionErrorException(e);
        }
    }

    private String getTeamupDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private class TeamupConnectionErrorException extends RuntimeException {
        public TeamupConnectionErrorException(UnirestException e) {
            super(e);
        }
    }
}
