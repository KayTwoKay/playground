package com.weather.api.model.dto;

import java.time.LocalDateTime;

public record WeatherDataDTO(Double temperature, Double humidity, Double windSpeed, Double windDirection,
                             Double precipitation, String location, LocalDateTime asOf) {
}
