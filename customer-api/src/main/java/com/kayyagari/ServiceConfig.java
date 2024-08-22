package com.kayyagari;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories({ "com.kayyagari" })
@EntityScan({ "com.kayyagari" })
@ComponentScan({ "com.kayyagari" })
public class ServiceConfig {

}
