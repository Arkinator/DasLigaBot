package com.planed.ctlBot.domain;

import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.common.Race;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by Julian Peters on 28.04.16.
 *
 * @author julian.peters@westernacher.com
 */
public class UserFixtures {
    public static User aDefaultUser() {
        final User user = new User();
        user.setDiscordId(RandomStringUtils.randomAlphabetic(20));
        user.setRace(Race.ZERG);
        user.setAccessLevel(AccessLevel.USER);
        return user;
    }

    public static User anAdmin() {
        final User user = new User();
        user.setDiscordId(RandomStringUtils.randomAlphabetic(20));
        user.setRace(Race.ZERG);
        user.setAccessLevel(AccessLevel.ADMIN);
        return user;
    }
}
