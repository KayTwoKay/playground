package com.weather.api;

import com.weather.api.exceptions.BadInputException;
import com.weather.api.model.entity.Sensor;
import com.weather.api.model.entity.WeatherData;
import com.weather.api.repository.SensorRepository;
import com.weather.api.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTests {

    @InjectMocks
    WeatherService weatherService;

    @Mock
    SensorRepository sensorRepository;


    @Test
    void testGetAverageWindSpeed() {
        var weatherData = new WeatherData();
        var weatherData2 = new WeatherData();
        weatherData.setWindSpeed(10.0);
        weatherData2.setWindSpeed(27.5);

        Map.Entry<String, List<WeatherData>> entry = Map.entry("location", List.of(weatherData, weatherData2));

        assertEquals(18.75, weatherService.getAverageWindSpeed(entry), 0.001);
    }


    //Just Showing mocking of repository
    @Test
    void testValidateSensorExists() {
        when(sensorRepository.findById("location")).thenReturn(Optional.of(new Sensor()));
        assertNotNull(weatherService.validateSensorExists("location"));
    }

    @Test
    void testValidateDateRangeValidInput() {
        assertTrue(weatherService.validateDateRange(LocalDate.now(), LocalDate.now().plusDays(10)));
    }

    @Test
    void testValidateDateRangeBadInput() {
        assertThrows(BadInputException.class, () ->
                weatherService.validateDateRange(LocalDate.now(), LocalDate.now().plusDays(32)));
    }

    @Test
    void testValidateWindSpeedValid() {
        assertTrue(weatherService.validateWindSpeed(50.0));
    }

    @Test
    void testValidateWindSpeedLowerLimit() {
        assertTrue(weatherService.validateWindSpeed(0.0));
    }

    @Test
    void testValidateWindSpeedUpperLimit() {
        assertTrue(weatherService.validateWindSpeed(1000.0));
    }

    @Test
    void testValidateWindSpeedBelowLowerLimit() {
        assertThrows(BadInputException.class, () -> weatherService.validateWindSpeed(-1.0));
    }

    @Test
    void testValidateWindSpeedAboveUpperLimit() {
        assertThrows(BadInputException.class, () -> weatherService.validateWindSpeed(1001.0));
    }
}
