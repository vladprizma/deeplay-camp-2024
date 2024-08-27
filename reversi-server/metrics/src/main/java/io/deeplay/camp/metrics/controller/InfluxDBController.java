package io.deeplay.camp.metrics.controller;

import io.deeplay.camp.metrics.service.InfluxDBService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
public class InfluxDBController {
    private final InfluxDBService influxDBService;

    public InfluxDBController(InfluxDBService influxDBService) {
        this.influxDBService = influxDBService;
    }

    @PutMapping("/insert")
    public String insertRandomValue() {
        influxDBService.insertRandomTemperature();
        return "Random temperature value inserted";
    }
}
