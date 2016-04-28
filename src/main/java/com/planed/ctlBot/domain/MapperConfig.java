package com.planed.ctlBot.domain;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;

public class MapperConfig {
    /**
     * Bean that defines all mapper that transform domain objects to entities.
     *
     * @return a Dozer mapper
     */
    @Bean
    public Mapper entityMapper() {
        final DozerBeanMapper mapper = new DozerBeanMapper();
        return mapper;
    }
}
