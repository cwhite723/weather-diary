package com.hayan.weatherdiary.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "weatherApi", url = "api.openweathermap.org/data/2.5")
public interface WeatherApiClient {

    @GetMapping("/weather")
    String getWeather(@RequestParam("lang") String lang,
                      @RequestParam("q") String city,
                      @RequestParam("appid") String apiKey);
}
