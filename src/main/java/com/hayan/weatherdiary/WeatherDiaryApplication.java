package com.hayan.weatherdiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
public class WeatherDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherDiaryApplication.class, args);
	}

}
