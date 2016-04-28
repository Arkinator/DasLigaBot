package com.planed.ctlBot.discord;

import com.planed.ctlBot.commands.data.CommandCall;
import com.planed.ctlBot.domain.User;
import com.planed.ctlBot.domain.UserRepository;
import com.planed.ctlBot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julian Peters on 23.04.16.
 *
 * @author julian.peters@westernacher.com
 */
@Service
public class CommandRegistry {
    private final ApplicationContext applicationContext;
    private final UserService userService;
    private final Map<String, DiscordCommand> commandNameMap;
    private final Map<DiscordCommand, Method> commandMap;
    private final Map<DiscordCommand, Object> controllerMap;
    private final UserRepository userRepository;

    @Autowired
    public CommandRegistry(final ApplicationContext applicationContext,
                           final UserService userService,
                           final UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.userService = userService;
        this.userRepository = userRepository;
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
    }

    public void fireEvent(final CommandCall call) {
        final DiscordCommand command = commandNameMap.get(call.getCommandPhrase());
        if (command == null) {
            throw new UnknownCommandException(call.getCommandPhrase());
        }

        checkUserAuthorization(call, command);

        invokeCommand(call, command);
    }

    private void checkUserAuthorization(final CommandCall call, final DiscordCommand command) {
        final User user = call.getAuthor();
        if (user.getAccessLevel().ordinal() < command.roleRequired().ordinal()) {
            throw new InsufficientAccessRightsException(command);
        }
        user.setNumberOfInteractions(user.getNumberOfInteractions() + 1);
        userRepository.save(user);
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
