package com.planed.ctlBot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Julian Peters on 09.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(BotBoot.class)
public class CtlDataStoreIntegrationTest {
    @Autowired
    CtlDataStore ctlDataStore;

    @Test
    public void testCtlDataStoreCorrectlyWired() {
        assertThat(ctlDataStore, not(nullValue()));
    }
}
