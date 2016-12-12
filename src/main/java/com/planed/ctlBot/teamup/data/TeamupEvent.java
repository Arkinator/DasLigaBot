package com.planed.ctlBot.teamup.data;

import java.time.OffsetDateTime;

/**
 * Created by julian.peters on 12/12/2016.
 */
public class TeamupEvent {
    private String title;
    private OffsetDateTime start;
    private OffsetDateTime end;
    private String who;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OffsetDateTime getStart() {
        return start;
    }

    public void setStart(OffsetDateTime start) {
        this.start = start;
    }

    public OffsetDateTime getEnd() {
        return end;
    }

    public void setEnd(OffsetDateTime end) {
        this.end = end;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
