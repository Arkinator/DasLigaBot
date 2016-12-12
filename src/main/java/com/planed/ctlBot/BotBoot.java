package com.planed.ctlBot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"sx.blah.discord", "com.planed.ctlBot"})
public class BotBoot {
    public static void main(final String[] args) {
        SpringApplication.run(BotBoot.class, args);
    }
}
