package com.planed.ctlBot;

import com.planed.ctlBot.data.CtlMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Component
public class CtlDataStore {
    Logger LOG = LoggerFactory.getLogger(CtlDataStore.class);

    private List<CtlMatch> matches = new ArrayList<>();

    public void addMatch(IUser user1, IUser user2) {
        matches.add(new CtlMatch(user1, user2));
        LOG.info("Added Match between "+user1+" and "+user2);
    }

    public List<CtlMatch> getMatches() {
        return matches;
    }
}
