package com.hayan.weatherdiary.service;

import com.hayan.weatherdiary.domain.Diary;
import com.hayan.weatherdiary.domain.Weather;
import com.hayan.weatherdiary.domain.dto.response.DiariesByDateResponse;
import com.hayan.weatherdiary.domain.dto.response.GetDiaryResponse;
import com.hayan.weatherdiary.exception.CustomException;
import com.hayan.weatherdiary.exception.ErrorCode;
import com.hayan.weatherdiary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final Logger logger = LoggerFactory.getLogger(DiaryService.class);

    private final WeatherService weatherService;
    private final DiaryRepository diaryRepository;

    @Transactional
    public void save(LocalDate date, String text) {
        logger.info("Method save() started with date: {}, text: {}", date, text);

        var weather = weatherService.getWeatherByDate(date);
        var diary = new Diary(weather, text);

        weather.addDiary(diary);
        diaryRepository.save(diary);
    }

    @Transactional(readOnly = true)
    public DiariesByDateResponse searchDiariesByDate(LocalDate date) {
        logger.info("Method searchDiariesByDate() started with date: {}", date);

        var weather = weatherService.getWeatherByDate(date);
        var foundDiaries = weather.getDiaries();

        List<GetDiaryResponse> diaryResponses = foundDiaries.stream()
                .map(diary -> new GetDiaryResponse(diary.getId(), diary.getText()))
                .toList();

        return new DiariesByDateResponse(date, weather, diaryResponses);
    }

    @Transactional(readOnly = true)
    public List<DiariesByDateResponse> searchDiariesByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Method searchDiariesByDateRange() started with startDate: {}, endDate: {}", startDate, endDate);

        if (startDate.isAfter(endDate)) throw new CustomException(ErrorCode.DATE_RANGE_ERROR);

        var weathers = weatherService.getWeathersByDateRange(startDate, endDate);

        List<DiariesByDateResponse> diaryResponses = new ArrayList<>();

        for (Weather weather : weathers) {
            if (weather.getDiaries().isEmpty()) continue;

            List<GetDiaryResponse> diaries = weather.getDiaries().stream()
                    .map(diary -> new GetDiaryResponse(diary.getId(), diary.getText()))
                    .collect(Collectors.toList());

            diaryResponses.add(new DiariesByDateResponse(weather.getDate(), weather, diaries));
        }

        return diaryResponses;
    }

    @Transactional
    public void edit(LocalDate date, String text) {
        logger.info("Method edit() started with date: {}, text: {}", date, text);

        var weather = weatherService.getWeatherByDate(date);
        var latestDiary = weather.getDiaries().stream()
                .max(Comparator.comparing(Diary::getId))
                .orElseThrow(() -> new CustomException(ErrorCode.DIARY_NOT_FROUND));

        latestDiary.edit(text);
    }

    @Transactional
    public void deleteDiariesByDate(LocalDate date) {
        logger.info("Method deleteDiariesByDate() started with date: {}", date);

        var weather = weatherService.getWeatherByDate(date);
        diaryRepository.deleteAllInBatch(weather.getDiaries());
    }
}
