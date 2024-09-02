package com.hayan.weatherdiary.repository;

import com.hayan.weatherdiary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
