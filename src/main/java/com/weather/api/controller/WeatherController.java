package com.weather.api.controller;

import com.weather.api.model.dto.*;
import com.weather.api.service.SensorService;
import com.weather.api.service.WeatherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService service) {
        this.weatherService = service;
    }

    @PostMapping(path = "/addWeather")
    public AddWeatherResponseDTO addWeatherData(@RequestBody AddWeatherDTO payload){
        return weatherService.addWeatherData(payload);
    }

    @PostMapping(path = "/getWeather")
    public GetWeatherResponseDTO getWeatherForSensors(@RequestBody GetWeatherForSensorsDTO payload ){
        return weatherService.getWeatherData(payload);
    }
}
