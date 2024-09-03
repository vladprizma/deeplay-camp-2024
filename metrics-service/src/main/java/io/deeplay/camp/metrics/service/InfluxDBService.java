package io.deeplay.camp.metrics.service;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class InfluxDBService implements MetricsStrategy {
    private final InfluxDB influxDB;

    public InfluxDBService(
            @Value("${influxdb.url}") String influxDBUrl,
            @Value("${influxdb.user}") String influxDBUser,
            @Value("${influxdb.password}") String influxDBPassword) {

        this.influxDB = InfluxDBFactory.connect(influxDBUrl, influxDBUser, influxDBPassword);
        this.influxDB.setDatabase("influxDB");
    }

    public void insertResponseTime(long responseTime, int totalUsers, int serverId) {
        Point point = Point.measurement("response_time")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("value", responseTime)
                .addField("total_users", totalUsers)
                .addField("server_id", serverId)
                .build();
        influxDB.write(point);
    }

    public void insertThroughput(int throughput, int serverId) {
        Point point = Point.measurement("throughput")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("value", throughput)
                .addField("server_id", serverId)
                .build();
        influxDB.write(point);
    }

    public void insertErrorRate(double errorRate, int serverId) {
        Point point = Point.measurement("error_rate")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("value", errorRate)
                .addField("server_id", serverId)
                .build();
        influxDB.write(point);
    }

    public void insertGameSessionsCount(int gameSessionsCount, int serverId) {
        Point point = Point.measurement("game_sessions_count")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("value", gameSessionsCount)
                .addField("server_id", serverId)
                .build();
        influxDB.write(point);
    }
        
    public void insertBotResponseTime(long botResponseTime, int botDepth, int serverId) {
        Point point = Point.measurement("bot_response_time")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("response_time", botResponseTime)
                .addField("bot_depth", botDepth)
                .addField("server_id", serverId)
                .build();
        influxDB.write(point);
    }

    public void insertServerLoad(double cpuUsage, double memoryUsage, double diskUsage, int serverId) {
        Point point = Point.measurement("server_load")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("cpu_usage", cpuUsage)
                .addField("memory_usage", memoryUsage)
                .addField("disk_usage", diskUsage)
                .addField("server_id", serverId)
                .build();
        influxDB.write(point);
    }
}