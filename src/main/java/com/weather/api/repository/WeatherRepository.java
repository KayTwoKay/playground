package com.weather.api.repository;

import com.weather.api.model.entity.Sensor;
import com.weather.api.model.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, Long> {

    @Query("SELECT w from WeatherData w WHERE w.location = :sensor AND w.ingestedOn >= :startDate AND w.ingestedOn <= endDate")
    List<WeatherData> getWeatherBySensorForRange(@Param("location") String location, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT w from WeatherData w WHERE w.location = :location AND w.ingestedOn = :latestDate")
    List<WeatherData> getLatestWeatherBySensor(@Param("location") String location, @Param("latestDate") LocalDateTime latestDate);

    @Query("SELECT MAX(w.ingestedOn) WHERE w.location = :location")
    LocalDateTime getLatestDataDateForSensor(@Param("location") String location);
}
