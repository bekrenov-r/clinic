package com.bekrenov.clinic.config;

import java.time.LocalDate;
import java.time.LocalTime;

import com.bekrenov.clinic.util.serializer.LocalDateSerializer;
import com.bekrenov.clinic.util.serializer.LocalTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializerByType(LocalTime.class, new LocalTimeSerializer());
        builder.serializerByType(LocalDate.class, new LocalDateSerializer());
        return builder;
    }
}
