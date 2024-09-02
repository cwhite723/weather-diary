package com.hayan.weatherdiary.service;

import com.hayan.weatherdiary.domain.Diary;
import com.hayan.weatherdiary.domain.Weather;
import com.hayan.weatherdiary.exception.CustomException;
import com.hayan.weatherdiary.exception.ErrorCode;
import com.hayan.weatherdiary.repository.DiaryRepository;
import com.hayan.weatherdiary.repository.WeatherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DiaryServiceTest {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private DiaryRepository diaryRepository;

    private Weather weather;

    @BeforeEach
    void setUp() {
        LocalDate date = LocalDate.of(2024, 10, 10);
        weather = new Weather(date, "clear", "01n", 298.44);
        weatherRepository.save(weather);
    }

    @AfterEach
    void cleanUp() {
        diaryRepository.deleteAllInBatch();
        weatherRepository.deleteAllInBatch();
    }

    @Test
    void 날짜와_텍스트로_일기를_작성할_수_있다() {
        // Given
        LocalDate date = LocalDate.of(2024, 10, 10);
        String text = "오늘의 일기 ~~~";

        // When
        diaryService.save(date, text);

        // Then
        List<Diary> diaries = diaryRepository.findAll();
        assertEquals(1, diaries.size());
        assertEquals(text, diaries.get(0).getText());
    }

    @Test
    void 날씨가_저장되지_않은_날짜에_일기를_쓰면_예외가_발생한다() {
        // Given
        LocalDate date = LocalDate.of(2024, 10, 11);
        String text = "오늘의 일기 ~~~";

        // When & Then
        assertThatThrownBy(() -> diaryService.save(date, text))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.WEATHER_NOT_FOUND);
    }

    @Test
    void 날짜와_텍스트로_해당_날짜의_가장_최근_일기를_수정할_수_있다() {
        // Given
        LocalDate date = LocalDate.of(2024, 10, 10);
        String text1 = "일기 1번";
        String text2 = "일기 2번";
        diaryService.save(date, text1);
        diaryService.save(date, text2);

        // When
        diaryService.edit(date, "수정된 일기");
        var diaries = diaryService.searchDiariesByDate(date)
                .getDiaries();

        // Then
        assertEquals("일기 1번", diaries.get(0).text());
        assertEquals("수정된 일기", diaries.get(1).text());
    }

    @Test
    void 해당_날짜의_일기가_없다면_예외가_발생한다() {
        // Given
        LocalDate date = LocalDate.of(2024, 10, 10);

        // When && Then
        assertThatThrownBy(() -> diaryService.edit(date, "수정된 일기"))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DIARY_NOT_FROUND);
    }

    @Test
    void 날짜로_해당_날짜의_일기를_전체_삭제할_수_있다() {
        // Given
        LocalDate date = LocalDate.of(2024, 10, 10);
        String text1 = "일기 1번";
        String text2 = "일기 2번";
        diaryService.save(date, text1);
        diaryService.save(date, text2);

        // When
        diaryService.deleteDiariesByDate(date);
        var diaries = diaryService.searchDiariesByDate(date).getDiaries();

        // Then
        assertEquals(diaries.size(), 0);
    }
}