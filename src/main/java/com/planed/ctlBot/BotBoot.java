package com.planed.ctlBot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by jules on 09.04.2016.
 */
@SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
public class BotBoot {
    public static void main(String[] args) {
        SpringApplication.run(BotBoot.class, args);
    }
}
