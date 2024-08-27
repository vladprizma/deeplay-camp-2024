package io.deeplay.camp.metrics.service;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@Service
public class InfluxDBService {
    private final InfluxDB influxDB;

    public InfluxDBService(
            @Value("${influxdb.url}") String influxDBUrl,
            @Value("${influxdb.user}") String influxDBUser,
            @Value("${influxdb.password}") String influxDBPassword) {

        this.influxDB = InfluxDBFactory.connect(influxDBUrl, influxDBUser, influxDBPassword);
        this.influxDB.setDatabase("influxDB");
    }

    public void insertRandomTemperature() {
        int temperature = 15 + (int) (Math.random() * (40 - 15 + 1));

        Point point = Point.measurement("temperature")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("value", temperature)
                .tag("location", "room1") 
                .build();

        influxDB.write(point);
    }
}
