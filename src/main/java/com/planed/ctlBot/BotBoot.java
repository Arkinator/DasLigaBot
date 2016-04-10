package com.planed.ctlBot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

;

/**
 * Created by jules on 09.04.2016.
 */
@Configuration
@ComponentScan(basePackages = "com.planed.ctlBot")
@PropertySource("/application.properties")
@EnableAutoConfiguration
public class BotBoot {
    public static void main(String[] args) {
//        System.setProperty("derby.system.homeSystem.setProp", "/Users/jps/code/CtlBattleBot/");
        SpringApplication.run(BotBoot.class, args);
    }

    @Bean
    public DataSource db() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        ds.setUrl("jdbc:derby:/Users/jps/code/CtlBattleBot/database.db");
        ds.setUsername("");
        ds.setPassword("");
        return ds;
//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        builder.setType(DERBY);
//        return builder.build();
    }
}
