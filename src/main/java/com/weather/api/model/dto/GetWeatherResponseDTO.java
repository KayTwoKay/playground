package com.weather.api.model.dto;


import java.util.List;

public record GetWeatherResponseDTO(List<WeatherDataDTO> data, List<MetricsDTO> metrics) {
}
