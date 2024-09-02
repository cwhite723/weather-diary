package com.hayan.weatherdiary.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum ErrorCode {
    // 400 Bad Request
    REQUEST_VALIDATION_FAIL(BAD_REQUEST, "잘못된 요청 값입니다."),
    JSON_PARSE_ERROR(BAD_REQUEST, "json 파싱에 실패했습니다."),
    DATE_RANGE_ERROR(BAD_REQUEST, "시작 날짜는 종료 날짜보다 빨라야 합니다."),

    // 403 Forbidden

    // 404 Not Found
    WEATHER_NOT_FOUND(NOT_FOUND, "해당 날짜의 날씨를 찾을 수 없습니다."),
    DIARY_NOT_FROUND(NOT_FOUND, "해당 날짜의 일기를 찾을 수 없습니다."),

    // 409 Conflict

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
