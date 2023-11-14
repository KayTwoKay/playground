package com.weather.api.service;

import com.weather.api.exceptions.SensorAlreadyExistsException;
import com.weather.api.model.dto.AddSensorDTO;
import com.weather.api.model.dto.AddSensorResponseDTO;
import com.weather.api.model.entity.Sensor;
import com.weather.api.repository.SensorRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public AddSensorResponseDTO addSensor(AddSensorDTO payload) {
        if (sensorRepository.findById(payload.location()).isPresent()) {
            throw new SensorAlreadyExistsException("Sensor is already present in the db.");
        }
        sensorRepository.save(new Sensor(payload.location(), null));

        return new AddSensorResponseDTO("Sensor added for: " + payload.location());
    }
}
