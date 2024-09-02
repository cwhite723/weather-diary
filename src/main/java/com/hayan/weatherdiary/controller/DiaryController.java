package com.hayan.weatherdiary.controller;

import com.hayan.weatherdiary.common.ApplicationResponse;
import com.hayan.weatherdiary.domain.dto.response.DiariesByDateResponse;
import com.hayan.weatherdiary.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Diary", description = "일기 CRUD api")
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/create/diary")
    @Operation(summary = "일기 작성 api", description = "일기를 작성한다.")
    public ApplicationResponse<Void> createDiary(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                 @RequestBody String text) {
        diaryService.save(date, text);

        return ApplicationResponse.noData();
    }

    @GetMapping("/read/diary")
    @Operation(summary = "특정 날짜의 일기 조회 api", description = "입력한 날짜의 일기를 모두 조회한다.")
    public ApplicationResponse<DiariesByDateResponse> getDiariesByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var diaries = diaryService.searchDiariesByDate(date);

        return ApplicationResponse.ok(diaries);
    }

    @GetMapping("read/diaries")
    @Operation(summary = "특정 기간의 일기 조회 api", description = "특정 기간의 일기를 모두 조회한다.")
    public ApplicationResponse<List<DiariesByDateResponse>> getDiariesByDateRange(@RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                                  @RequestParam("end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var diariesInRange = diaryService.searchDiariesByDateRange(startDate, endDate);

        return ApplicationResponse.ok(diariesInRange);
    }

    @PutMapping("/update/diary")
    @Operation(summary = "일기 수정 api", description = "특정 날짜의 가장 최근에 작성된 일기 내용을 수정한다.")
    public ApplicationResponse<Void> updateDiary(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                 @RequestBody String text) {
        diaryService.edit(date, text);

        return ApplicationResponse.noData();
    }

    @DeleteMapping("/delete/diary")
    @Operation(summary = "일기 삭제 api", description = "특정 날짜의 일기를 전체 삭제한다.")
    public ApplicationResponse<Void> deleteDiary(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        diaryService.deleteDiariesByDate(date);

        return ApplicationResponse.noData();
    }
}
