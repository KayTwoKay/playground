package com.weather.api.model.dto;

public record MetricsDTO(Double averageTemperature, Double averageHumidity, Double precipitationSum,
                         Double averageWindSpeed, Double averageWindDirection,  String location) {
}
