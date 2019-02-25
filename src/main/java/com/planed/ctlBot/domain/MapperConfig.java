package com.planed.ctlBot.domain;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;

public class MapperConfig {
    @Bean
    public Mapper entityMapper() {
        final DozerBeanMapper mapper = new DozerBeanMapper();
        return mapper;
    }
}
