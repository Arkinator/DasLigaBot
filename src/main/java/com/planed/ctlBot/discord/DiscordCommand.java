package com.planed.ctlBot.discord;

import com.planed.ctlBot.common.AccessLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denotes a command to be registered with discord.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DiscordCommand {
    /**
     * The name of the command
     */
    String[] name();

    /**
     * A help text describing this command
     */
    String help() default "";

    /**
     * The AccessLevel required to invoke this command
     */
    AccessLevel roleRequired() default AccessLevel.USER;

    int minMentions() default 0;
}
