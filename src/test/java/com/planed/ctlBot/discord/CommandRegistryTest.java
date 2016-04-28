package com.planed.ctlBot.discord;

/**
 * Created by Julian Peters on 23.04.16.
 *
 * @author julian.peters@westernacher.com
 */

import com.planed.ctlBot.BotBoot;
import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.commands.data.CommandCallBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

@SpringApplicationConfiguration(classes = {
        BotBoot.class,
        CommandRegistry.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class CommandRegistryTest {
    private static List<Object> callParameters;

    @Autowired
    private CommandRegistry commandRegistry;

    @Before
    public void setUp() {
        callParameters = new ArrayList<>();
    }

    @Test
    public void shouldCallTestCommandIfCallTriggered(){
        final CommandCall call = new CommandCallBuilder().setCommandPhrase("test").createCommandCall();
        assertThat(callParameters, is(empty()));
        commandRegistry.fireEvent(call);
        assertThat(callParameters, is(not(empty())));
        assertThat(callParameters.get(0), is(call));
    }

    @Test
    public void shouldCallTestCommandForMultiNameMethod(){
        final CommandCall call1 = new CommandCallBuilder().setCommandPhrase("test1").createCommandCall();
        commandRegistry.fireEvent(call1);
        assertThat(callParameters.get(0), is(call1));

        callParameters.remove(0);

        final CommandCall call2 = new CommandCallBuilder().setCommandPhrase("test2").createCommandCall();
        commandRegistry.fireEvent(call2);
        assertThat(callParameters.get(0), is(call2));
    }

    @DiscordController
    public static class TestClass {
        @DiscordCommand(name="test")
        public void testCommand1(final CommandCall call){
            callParameters.add(call);
        }

        @DiscordCommand(name={"test1", "test2"})
        public void testCommand2(final CommandCall call){
            callParameters.add(call);
        }
    }
}
