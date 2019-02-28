package com.planed.ctlBot.discord;

import com.planed.ctlBot.commands.data.DiscordMessage;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.UserService;
import com.planed.ctlBot.utils.DiscordMessageParser;
import org.apache.commons.lang3.ArrayUtils;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommandRegistry implements MessageCreateListener {
    private static final Logger logger = LoggerFactory.getLogger(CommandRegistry.class);

    private final Map<String, DiscordCommand> commandNameMap = new HashMap<>();
    private final Map<DiscordCommand, Method> commandMap = new HashMap<>();
    private final Map<DiscordCommand, Object> controllerMap = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DiscordService discordService;
    @Autowired
    private UserService userService;
    @Autowired
    private DiscordApi discordApi;
    @Autowired
    private DiscordMessageParser discordMessageParser;
    @Value("${discord.authorUserId}")
    private String authorUserId;

    @PostConstruct
    public void registerDiscordControllers() {
        discordApi.addMessageCreateListener(this);

        findAndCollectDiscordCommandBeans();

        buildCommandList();

        promoteFustup();
    }

    private void findAndCollectDiscordCommandBeans() {
        final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(DiscordController.class);
        for (final Object bean : beans.values()) {
            for (final Method method : bean.getClass().getMethods()) {
                final DiscordCommand command = method.getAnnotation(DiscordCommand.class);
                if (command != null) {
                    registerDiscordCommand(command, method, bean);
                }
            }
        }
    }

    private void promoteFustup() {
        userService.giveUserAccessLevel(authorUserId, AccessLevel.AUTHOR);
    }

    private String buildCommandList() {
        final StringBuilder builder = new StringBuilder();
        for (final DiscordCommand command : getAllCommands()) {
            builder.append(ArrayUtils.toString(command.name()));
            builder.append("\t\t");
            builder.append(command.help());
            builder.append("\n");

        }
        return builder.toString();
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessageAuthor().isYourself())
            return;

        discordMessageParser.deconstructMessage(event.getMessage())
                .ifPresent(this::fireEvent);
    }

    public void fireEvent(final DiscordMessage call) {
        final DiscordCommand command = commandNameMap.get(call.getCommandPhrase());
        logger.info("Command from " + call.getAuthor()
                + " with command " + call.getCommandPhrase()
                + " and Mentions " + call.getMentions()
                + " and Parameters " + call.getParameters());
        final User user = call.getAuthor();

        if (command != null
                && checkUserAuthorization(call, command, user)
                && checkMinimumMentions(call, command)
                && checkMinimumParameters(call, command)) {
            userService.incrementCallsForUserByDiscordId(user.getDiscordId());
            invokeCommand(call, command);
        }
    }

    private boolean checkMinimumParameters(DiscordMessage call, DiscordCommand command) {
        if (call.getParameters().size() < command.minParameters()) {
            discordService.whisperToUser(call.getAuthor().getDiscordId(),
                    "You need " + command.minParameters() + " parameters for this command");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkMinimumMentions(DiscordMessage call, DiscordCommand command) {
        if (call.getMentions().size() < command.minMentions()) {
            discordService.whisperToUser(call.getAuthor().getDiscordId(),
                    "You need " + command.minMentions() + " mention (type @ and a username) as a parameter to this command");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkUserAuthorization(final DiscordMessage call, final DiscordCommand command, User user) {
        if (user.getAccessLevel().ordinal() < command.roleRequired().ordinal()) {
            discordService.whisperToUser(call.getAuthor().getDiscordId(),
                    "Insufficent access rights to invoke command!");
            return false;
        } else {
            return true;
        }
    }

    private void invokeCommand(final DiscordMessage call, final DiscordCommand command) {
        final Method method = commandMap.get(command);
        try {
            method.invoke(controllerMap.get(command), call);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InternalServerException(e);
        }
    }

    private void registerDiscordCommand(final DiscordCommand command, final Method method, final Object bean) {
        commandMap.put(command, method);
        for (final String commandName : command.name()) {
            commandNameMap.put(commandName, command);
        }
        controllerMap.put(command, bean);
    }

    public Collection<DiscordCommand> getAllCommands() {
        return commandMap.keySet();
    }

    private class InternalServerException extends RuntimeException {
        InternalServerException(final Exception e) {
            super(e);
        }
    }
}
