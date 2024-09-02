package com.hayan.weatherdiary.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "weathers")
@NoArgsConstructor(access = PROTECTED)
public class Weather {

    @Id
    @Column(name = "weather_date")
    private LocalDate date;

    private String weather;

    private String icon;

    private Double temperature;

    @OneToMany(mappedBy = "weather", orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();

    @Builder
    public Weather(LocalDate date, String weather, String icon, Double temperature) {
        this.date = date;
        this.weather = weather;
        this.icon = icon;
        this.temperature = temperature;
    }

    public void addDiary(Diary diary) {
        this.diaries.add(diary);
    }
}
