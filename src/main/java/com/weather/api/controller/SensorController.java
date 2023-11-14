package com.weather.api.controller;

import com.weather.api.model.dto.*;
import com.weather.api.service.SensorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/sensor")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping(path = "/addSensor")
    public AddSensorResponseDTO addSensor(@RequestBody AddSensorDTO payload) {
        return new AddSensorResponseDTO(null);
    }
}
