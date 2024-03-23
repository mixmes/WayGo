package ru.sfedu.server.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import static org.springframework.util.unit.DataSize.of;

@Configuration
public class MutipartConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(of(1L, DataUnit.GIGABYTES));
        factory.setMaxRequestSize(of(1L,DataUnit.GIGABYTES));
        return factory.createMultipartConfig();
    }
}
