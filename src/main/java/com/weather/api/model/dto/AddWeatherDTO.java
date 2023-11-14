package com.weather.api.model.dto;


public record AddWeatherDTO(Double temperature, Double humidity, Double windSpeed, Double windDirection,
                            Double precipitation, String sensorLocation) {
}
