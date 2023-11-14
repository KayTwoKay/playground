package com.weather.api.repository;

import com.weather.api.model.entity.Sensor;
import com.weather.api.model.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, Long> {

    @Query("SELECT w FROM WeatherData w WHERE w.sensor.location = :location AND w.ingestedOn >= :startDate AND w.ingestedOn <= :endDate")
    List<WeatherData> getWeatherBySensorForRange(@Param("location") String location, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT w FROM WeatherData w WHERE w.sensor.location = :location AND w.ingestedOn = :latestDate")
    List<WeatherData> getLatestWeatherBySensor(@Param("location") String location, @Param("latestDate") LocalDateTime latestDate);

    @Query("SELECT MAX(w.ingestedOn) FROM WeatherData w WHERE w.sensor.location = :location")
    Optional<LocalDateTime> getLatestDataDateForSensor(@Param("location") String location);
}
