package com.planed.ctlBot.teamup;

import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Ignore("This is an integration test that requires connection to TeamUP")
public class TeamupClientTest {
    private final String apiKey = "bf3bb3cfc9a84f988e5c1a14cbb369a615b8ad78ea98232f5051a1b5fb5ec816";
    private final String teamUrCalendarId = "ks5f1fd3feec094935";

    @Test
    public void testApiKey() {
        TeamupClient teamupClient = TeamupClient.build(apiKey);
        assertThat(teamupClient.checkConnection(), is(true));
    }

    @Test
    public void queryTeamUrCalendar() {
        TeamupClient teamupClient = TeamupClient.build(apiKey);
        System.out.println(teamupClient.getEventsBetweenDates(teamUrCalendarId,
                LocalDate.of(2016, Month.JANUARY, 1),
                LocalDate.of(2017, Month.JANUARY, 1)));
    }
}
