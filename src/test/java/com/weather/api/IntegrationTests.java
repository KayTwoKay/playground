package com.weather.api;

import com.weather.api.controller.WeatherController;
import com.weather.api.model.dto.AddWeatherDTO;
import com.weather.api.model.dto.GetWeatherForSensorsDTO;
import com.weather.api.repository.WeatherRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@Transactional
@Sql(scripts = {"/scripts/import_sensors.sql"})
public class IntegrationTests {

    @Autowired
    WeatherController weatherController;

    @Autowired
    WeatherRepository weatherRepository;

    @Test
    public void testAddData(){
        weatherController.addWeatherData(new AddWeatherDTO(10.5, 25.0, 3.0, 120.0, 50.0, "castlebar"));
        Assertions.assertEquals(1, weatherRepository.findAll().size());
    }

    @Test
    public void testAddDataAndRetrieveLatest(){
        weatherController.addWeatherData(new AddWeatherDTO(10.5, 25.0, 3.0, 120.0, 50.0, "castlebar"));
        weatherController.addWeatherData(new AddWeatherDTO(11.5, 27.0, 3.5, 130.0, 50.0, "castlebar"));
        var latestData = weatherController.getWeatherForSensors(new GetWeatherForSensorsDTO(List.of("castlebar"), null, null)).data().get(0);
        Assertions.assertEquals(2, weatherRepository.findAll().size());
        Assertions.assertEquals(11.5, latestData.temperature());
        Assertions.assertEquals(27, latestData.humidity());
    }

    @Test
    public void testAddAndCalculateMetricsForWeek(){
        weatherController.addWeatherData(new AddWeatherDTO(10.0, 30.0, 3.0, 120.0, 50.0, "castlebar"));
        weatherController.addWeatherData(new AddWeatherDTO(15.0, 25.0, 3.5, 130.0, 50.0, "castlebar"));
        weatherController.addWeatherData(new AddWeatherDTO(20.0, 20.0, 4.0, 140.0, 0.0, "castlebar"));
        var metrics = weatherController.getWeatherForSensors(new GetWeatherForSensorsDTO(List.of("castlebar"), LocalDate.now(), LocalDate.now())).metrics().get(0);
        Assertions.assertEquals(3, weatherRepository.findAll().size());
        Assertions.assertEquals(25, metrics.averageHumidity());
        Assertions.assertEquals(15, metrics.averageTemperature());
        Assertions.assertEquals(3.5, metrics.averageWindSpeed());
        Assertions.assertEquals(100, metrics.precipitationSum());
        Assertions.assertEquals(130, metrics.averageWindDirection());
    }


}
