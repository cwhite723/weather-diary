package com.hayan.weatherdiary.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeatherScheduler {
    private final Logger logger = LoggerFactory.getLogger(WeatherScheduler.class);
    private final WeatherService weatherService;

    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeather() {
        logger.info("Scheduled task saveWeather() started");

        weatherService.save();
    }
}
