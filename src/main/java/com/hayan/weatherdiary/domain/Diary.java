package com.hayan.weatherdiary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "diaries")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Diary {
    @Id
    @Column(name = "diary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_date")
    private Weather weather;

    @Column(columnDefinition = "TEXT")
    private String text;

    public Diary(Weather weather, String text) {
        this.weather = weather;
        this.text = text;
    }

    public void edit(String text) {
        this.text = text;
    }
}
