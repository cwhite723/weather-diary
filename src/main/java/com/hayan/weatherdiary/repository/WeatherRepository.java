package com.hayan.weatherdiary.repository;

import com.hayan.weatherdiary.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Optional<Weather> findByDate(LocalDate date);
    List<Weather> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
