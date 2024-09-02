package com.hayan.weatherdiary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hayan.weatherdiary.controller.WeatherApiClient;
import com.hayan.weatherdiary.domain.Weather;
import com.hayan.weatherdiary.exception.CustomException;
import com.hayan.weatherdiary.exception.ErrorCode;
import com.hayan.weatherdiary.repository.WeatherRepository;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final ObjectMapper objectMapper;

    private final WeatherApiClient weatherApiClient;
    private final WeatherRepository weatherRepository;

    private final String weatherApiKey = Dotenv.load().get("WEATHER_API_KEY");
    private final String lang = "kr";
    private final String city = "seoul";

    @Transactional
    public void save() {
        logger.info("Method save() started");

        String jsonWeather = weatherApiClient.getWeather(lang, city, weatherApiKey);
        Weather weather = jsonToWeather(jsonWeather);

        weatherRepository.save(weather);
    }

    private Weather jsonToWeather(String jsonWeather) {
        logger.info("Method jsonToWeather() started with jsonWeather: {}", jsonWeather);

        Weather weatherEntity = null;

        try {
            JsonNode root = objectMapper.readTree(jsonWeather);

            String weather = root.get("weather").get(0).get("main").asText();
            String icon = root.get("weather").get(0).get("icon").asText();
            Double temperature = root.get("main").get("temp").asDouble();

            weatherEntity = Weather.builder()
                    .date(LocalDate.now())
                    .weather(weather)
                    .icon(icon)
                    .temperature(temperature)
                    .build();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_PARSE_ERROR);
        }

        return weatherEntity;
    }

    @Transactional(readOnly = true)
    public Weather getWeatherByDate(LocalDate date) {
        logger.info("Method getWeatherByDate() started with date: {}", date);

        Weather weather = weatherRepository.findByDate(date)
                .orElseThrow(() -> new CustomException(ErrorCode.WEATHER_NOT_FOUND));

        return weather;
    }

    @Transactional(readOnly = true)
    public List<Weather> getWeathersByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Method getWeathersByDateRange() started with startDate: {}, endDate: {}", startDate, endDate);

        return weatherRepository.findAllByDateBetween(startDate, endDate);
    }
}
