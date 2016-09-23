package com.planed.ctlBot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan
//@PropertySource("/application.properties")
//@EnableAutoConfiguration
public class BotBoot extends SpringBootServletInitializer {
    public static void main(final String[] args) {
        SpringApplication.run(BotBoot.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(applicationClass);
    }

    private static Class<BotBoot> applicationClass = BotBoot.class;
}
