package com.hayan.weatherdiary.controller;

import com.hayan.weatherdiary.common.ApplicationResponse;
import com.hayan.weatherdiary.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Weather", description = "날씨 api")
public class WeatherController {
    private final WeatherService weatherService;

    // 현재 날씨 수동으로 저장
    @PostMapping("/create/weather")
    @Operation(summary = "현재 날씨 저장 api", description = "조회한 시점의 날씨를 저장한다. 만약 동일한 날짜에 날씨가 있을 경우 날씨가 수정된다.")
    public ApplicationResponse<Void> createWeather() {
        weatherService.save();

        return ApplicationResponse.noData();
    }
}
