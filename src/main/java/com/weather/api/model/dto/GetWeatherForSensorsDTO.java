package com.weather.api.model.dto;

import java.time.LocalDate;
import java.util.List;

public record GetWeatherForSensorsDTO(List<String> sensorIds, LocalDate startDate, LocalDate endDate) {
}
