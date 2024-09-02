package com.hayan.weatherdiary.domain.dto.response;

import com.hayan.weatherdiary.domain.Weather;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DiariesByDateResponse {
    private final LocalDate date;
    private final String weather;
    private final String icon;
    private final Double temperature;
    private final List<GetDiaryResponse> diaries;

    public DiariesByDateResponse(LocalDate date, Weather weather, List<GetDiaryResponse> diaries) {
        this.date = date;
        this.weather = weather.getWeather();
        this.icon = weather.getIcon();
        this.temperature = weather.getTemperature();
        this.diaries = diaries;
    }
}
