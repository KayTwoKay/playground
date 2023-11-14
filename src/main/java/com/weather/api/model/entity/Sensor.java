package com.weather.api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sensor {

    @Id
    private String location;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "location")
    private List<WeatherData> data;
}
