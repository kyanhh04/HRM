package com.vatek.hrmtool;

import com.vatek.hrmtool.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@Log4j2
@EnableJpaAuditing
@AllArgsConstructor
public class HrmToolApplication {

    final Environment env;

    public static void main(String[] args) {
        SpringApplication.run(HrmToolApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setApplicationTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        log.info("TimeZone : {} , Instant : {} , Timestamp : {}", TimeZone.getDefault(), Instant.now(), Timestamp.from(DateUtils.getInstantNow()));
    }


    @EventListener(ApplicationReadyEvent.class)
    public void version() {
        log.info("Application Name : {}", () -> env.getProperty("application.name"));
        log.info("Build Version : {}", () -> env.getProperty("build.version"));
        log.info("Build Timestamp : {}", () -> env.getProperty("build.timestamp"));
    }
}
