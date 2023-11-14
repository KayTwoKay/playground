package com.weather.api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Celsius
    private Double temperature;

    // Percentage
    private Double humidity;

    // Miles per hour
    private Double windSpeed;

    // Degrees relative to 360
    private Double windDirection;

    // Millimeters
    private Double precipitation;

    @ManyToOne
    @JoinColumn(name = "location")
    private Sensor sensor;

    private LocalDateTime ingestedOn;
}
