package com.planed.ctlBot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jules on 09.04.2016.
 */
@Configuration
@ComponentScan(basePackages = "com.planed.ctlBot")
@EnableAutoConfiguration
public class BotBoot {
    public static void main(String[] args) {
        SpringApplication.run(BotBoot.class, args);
    }
}
