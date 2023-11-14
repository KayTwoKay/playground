package com.weather.api.service;

import com.weather.api.exceptions.BadInputException;
import com.weather.api.exceptions.ResourceNotFoundException;
import com.weather.api.model.dto.*;
import com.weather.api.model.entity.Sensor;
import com.weather.api.model.entity.WeatherData;
import com.weather.api.repository.SensorRepository;
import com.weather.api.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final SensorRepository sensorRepository;

    public WeatherService(WeatherRepository weatherRepository, SensorRepository sensorRepository) {
        this.weatherRepository = weatherRepository;
        this.sensorRepository = sensorRepository;
    }

    public AddWeatherResponseDTO addWeatherData(AddWeatherDTO data) {
        performInputValidation(data);
        var s = validateSensorExists(data.sensorLocation());
        weatherRepository.save(new WeatherData(null, data.temperature(), data.humidity(), data.windSpeed(),
                data.windDirection(), data.precipitation(), s, LocalDateTime.now()));
        return new AddWeatherResponseDTO("Weather successfully added for: " + data.sensorLocation());
    }

    public GetWeatherResponseDTO getWeatherData(GetWeatherForSensorsDTO payload) {
        payload.sensorIds().forEach(this::validateSensorExists);
        HashMap<String, List<WeatherData>> mapOfData = new HashMap<>();

        if (payload.startDate() == null || payload.endDate() == null) {
            getLatestWeather(payload, mapOfData);
        } else {
            validateDateRange(payload.startDate(), payload.endDate());
            getWeatherDataForRange(payload, mapOfData);
        }


        return new GetWeatherResponseDTO(mapModelToDTO(mapOfData), getMetricsDTOS(mapOfData));
    }

    private List<WeatherDataDTO> mapModelToDTO(HashMap<String, List<WeatherData>> mapOfData) {
        return mapOfData.values().stream()
                .flatMap(List::stream)
                .map(m -> new WeatherDataDTO(m.getTemperature(), m.getHumidity(), m.getWindSpeed(),
                        m.getWindDirection(), m.getPrecipitation(), m.getSensor().getLocation(), m.getIngestedOn()))
                .toList();
    }

    private ArrayList<MetricsDTO> getMetricsDTOS(HashMap<String, List<WeatherData>> mapOfData) {
        var listOfMetrics = new ArrayList<MetricsDTO>();

        for (Map.Entry<String, List<WeatherData>> entry : mapOfData.entrySet()) {

            var averageTemperature = getAverageTemperature(entry);
            var averageHumidity = getAverageHumidity(entry);
            var averageWindSpeed = getAverageWindSpeed(entry);
            var averageWindDirection = getAverageWindDirection(entry);
            var sumOfPrecipitation = getSumOfPrecipitation(entry);
            listOfMetrics.add(new MetricsDTO(averageTemperature, averageHumidity, sumOfPrecipitation, averageWindSpeed, averageWindDirection, entry.getKey()));
        }
        return listOfMetrics;
    }

    private void getWeatherDataForRange(GetWeatherForSensorsDTO payload, HashMap<String, List<WeatherData>> mapOfData) {
        for (String sensorId : payload.sensorIds()) {
            mapOfData.put(sensorId,
                    weatherRepository.getWeatherBySensorForRange(sensorId, payload.startDate().atStartOfDay(), payload.endDate().atTime(23, 59, 59)));
        }
    }

    private void getLatestWeather(GetWeatherForSensorsDTO payload, HashMap<String, List<WeatherData>> mapOfData) {
        for (String sensorId : payload.sensorIds()) {
            var latestTimeForData = weatherRepository.getLatestDataDateForSensor(sensorId).orElse(LocalDateTime.now());
            mapOfData.put(sensorId,
                    weatherRepository.getLatestWeatherBySensor(sensorId, latestTimeForData));
        }
    }

    private double getSumOfPrecipitation(Map.Entry<String, List<WeatherData>> entry) {
        return entry.getValue().stream()
                .mapToDouble(WeatherData::getPrecipitation)
                .sum();
    }

    private double getAverageWindDirection(Map.Entry<String, List<WeatherData>> entry) {
        return entry.getValue().stream()
                .mapToDouble(WeatherData::getWindDirection)
                .average()
                .orElse(0);
    }

    public double getAverageWindSpeed(Map.Entry<String, List<WeatherData>> entry) {
        return entry.getValue().stream()
                .mapToDouble(WeatherData::getWindSpeed)
                .average()
                .orElse(0);
    }

    private double getAverageHumidity(Map.Entry<String, List<WeatherData>> entry) {
        return entry.getValue().stream()
                .mapToDouble(WeatherData::getHumidity)
                .average()
                .orElse(0);
    }

    private double getAverageTemperature(Map.Entry<String, List<WeatherData>> entry) {
        return entry.getValue().stream()
                .mapToDouble(WeatherData::getTemperature)
                .average()
                .orElse(0);
    }

    public Sensor validateSensorExists(String location) {
        return sensorRepository.findById(location)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found"));
    }

    public boolean validateDateRange(LocalDate startDate, LocalDate endDate) {
        long daysBetween = DAYS.between(startDate, endDate);
        if (daysBetween > 30 || daysBetween < 0) {
            throw new BadInputException("Range is too large please provide a range between 1-30 days to query.");
        }
        return true;
    }

    public boolean validateTemperature(Double temperature) {
        if (temperature <= -90 || temperature >= 90) {
            throw new BadInputException("Abnormal temperatures please check in input.");
        }
        return true;
    }

    public boolean validateHumidity(Double humidity) {
        if (humidity < 0 || humidity > 100) {
            throw new BadInputException("Humidity cannot go below 0% or 100% please check input.");
        }
        return true;
    }

    public boolean validatePrecipitation(Double precipitation) {
        if (precipitation < 0 || precipitation > 1000) {
            throw new BadInputException("Precipitation cannot be below 0 or above 10000 please check input.");
        }
        return true;
    }

    public boolean validateWindSpeed(Double windSpeed) {
        if (windSpeed < 0 || windSpeed > 1000) {
            throw new BadInputException("Wind speed cannot be less than zero or greater than 1000 please check input.");
        }
        return true;
    }

    public boolean validateWindDirection(Double windDirection) {
        if (windDirection < 0 || windDirection > 360) {
            throw new BadInputException("Wind direction cannot be outside the possible 360 degrees please check input.");
        }
        return true;
    }

    public void performInputValidation(AddWeatherDTO data) {
        validateHumidity(data.humidity());
        validatePrecipitation(data.precipitation());
        validateWindDirection(data.windDirection());
        validateWindSpeed(data.windSpeed());
        validateTemperature(data.temperature());
    }
}
