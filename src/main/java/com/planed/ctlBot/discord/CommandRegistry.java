package com.planed.ctlBot.discord;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.common.AccessLevel;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.services.UserService;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommandRegistry {
    Logger LOG = LoggerFactory.getLogger(CommandRegistry.class);

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DiscordService discordService;
    @Autowired
    private UserService userService;

    private final Map<String, DiscordCommand> commandNameMap;
    private final Map<DiscordCommand, Method> commandMap;
    private final Map<DiscordCommand, Object> controllerMap;

    public CommandRegistry() {
        commandMap = new HashMap<>();
        commandNameMap = new HashMap<>();
        controllerMap = new HashMap<>();
    }

    @PostConstruct
    public void registerDiscordControllers() {
        final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(DiscordController.class);
        for (final Object bean : beans.values()) {
            for (final Method method : bean.getClass().getDeclaredMethods()) {
                final DiscordCommand command = method.getAnnotation(DiscordCommand.class);
                if (command == null) {
                    continue;
                } else {
                    registerDiscordCommand(command, method, bean);
                }
            }
        }
        buildCommandList();
        promoteFustup();
    }

    private void promoteFustup() {
        userService.giveUserAccessLevel("116296552204599298", AccessLevel.Author);
    }

    private void buildCommandList() {
        final StringBuilder builder = new StringBuilder();
        for (final DiscordCommand command : getAllCommands()) {
            builder.append(ArrayUtils.toString(command.name()));
            builder.append("\t\t");
            builder.append(command.help());
            builder.append("\n");

        }
        discordService.setCommandList(builder.toString());
    }

    public void fireEvent(final CommandCall call) {
        final DiscordCommand command = commandNameMap.get(call.getCommandPhrase());
        LOG.info("Command from " + call.getAuthor()
                + " with command " + call.getCommandPhrase()
                + " and Mentions " + call.getMentions()
                + " and Parameters " + call.getParameters());
        if (command != null && checkUserAuthorization(call, command)) {
            invokeCommand(call, command);
        }
    }

    private boolean checkUserAuthorization(final CommandCall call, final DiscordCommand command) {
        final User user = call.getAuthor();
        if (user.getAccessLevel().ordinal() < command.roleRequired().ordinal()) {
            discordService.whisperToUser(call.getAuthor().getDiscordId(),
                    "Insufficent access rights to invoke command!");
            return false;
        } else if (call.getMentions().size() < command.minMentions()) {
            discordService.whisperToUser(call.getAuthor().getDiscordId(),
                    "You need " + command.minMentions() + " mention (type @ and a username) as a parameter to this command");
            return false;
        } else {
            userService.incrementCallsForUserByDiscordId(user.getDiscordId());
            return true;
        }
    }

    private void invokeCommand(final CommandCall call, final DiscordCommand command) {
        final Method method = commandMap.get(command);
        try {
            method.invoke(controllerMap.get(command), call);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InternalServerException(e);
        }
    }

    public void registerDiscordCommand(final DiscordCommand command, final Method method, final Object bean) {
        commandMap.put(command, method);
        for (final String commandName : command.name()) {
            commandNameMap.put(commandName, command);
        }
        controllerMap.put(command, bean);
    }

    public Collection<DiscordCommand> getAllCommands() {
        return commandMap.keySet();
    }

    private class UnknownCommandException extends RuntimeException {
        public UnknownCommandException(final String commandName) {
            super("Unknown command encountered: " + commandName);
        }
    }

    private class InternalServerException extends RuntimeException {
        public InternalServerException(final Exception e) {
            super(e);
        }
    }

    public static class InsufficientAccessRightsException extends RuntimeException {
        public InsufficientAccessRightsException(final DiscordCommand command) {
            super("Insufficient access rights to invoke command " + command.name()[0] + ".");
        }
    }
}
