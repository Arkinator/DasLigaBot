package com.planed.ctlBot.teamup;

import com.planed.ctlBot.teamup.data.TeamupEvent;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TeamupServiceTest {
    @Mock
    TeamupClient teamupClient;
    @InjectMocks
    TeamupService teamupService;

    @Test
    public void shouldReturnNonEmptyListOfEventsForPastYear() throws IOException {
        doReturn(FileUtils.readFileToString(new File("src/test/resources/test.json"),"UTF-8"))
                .when(teamupClient).getEventsBetweenDates(any(),any(),any());

        List<TeamupEvent> resultList = teamupService.getEventsBetweenDates(null, null);
        assertThat(resultList).isNotEmpty();
        assertThat(resultList.get(0).getTitle()).isEqualTo("Lit-stomping");
        assertThat(resultList.get(0).getStart()).isEqualByComparingTo(OffsetDateTime.parse("2016-06-10T01:00:00-05:00"));
        assertThat(resultList.get(0).getEnd()).isEqualByComparingTo(OffsetDateTime.parse("2016-06-10T02:00:00-05:00"));
        assertThat(resultList.get(0).getWho()).isEqualTo("FusTup");
    }
}
