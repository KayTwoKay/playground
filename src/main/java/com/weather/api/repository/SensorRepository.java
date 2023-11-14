package com.weather.api.repository;

import com.weather.api.model.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository <Sensor, String> {
}
