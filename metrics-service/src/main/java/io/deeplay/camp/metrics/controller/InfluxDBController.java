package io.deeplay.camp.metrics.controller;

import io.deeplay.camp.metrics.service.InfluxDBService;
import io.deeplay.camp.metrics.service.MetricsStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
public class InfluxDBController {
    private final MetricsStrategy influxDBService;
    
    @Autowired
    public InfluxDBController(MetricsStrategy influxDBService) {
        this.influxDBService = influxDBService;
    }

    @PutMapping("/response-time")
    public ResponseEntity<String> insertResponseTime(@RequestParam long responseTime, @RequestParam int totalUsers, @RequestParam int serverId) {
        try {
            influxDBService.insertResponseTime(responseTime, totalUsers, serverId);
            return new ResponseEntity<>("Response time inserted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to insert response time", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/throughput")
    public ResponseEntity<String> insertThroughput(@RequestParam int throughput, @RequestParam int serverId) {
        try {
            influxDBService.insertThroughput(throughput, serverId);
            return new ResponseEntity<>("Throughput inserted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to insert throughput", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/error-rate")
    public ResponseEntity<String> insertErrorRate(@RequestParam double errorRate, @RequestParam int serverId) {
        try {
            influxDBService.insertErrorRate(errorRate, serverId);
            return new ResponseEntity<>("Error rate inserted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to insert error rate", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/game-sessions-count")
    public ResponseEntity<String> insertGameSessionsCount(@RequestParam int gameSessionsCount, @RequestParam int serverId) {
        try {
            influxDBService.insertGameSessionsCount(gameSessionsCount, serverId);
            return new ResponseEntity<>("Game sessions count inserted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to insert game sessions count", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/bot-response-time")
    public ResponseEntity<String> insertBotResponseTime(@RequestParam long botResponseTime, @RequestParam int botDepth, @RequestParam int serverId) {
        try {
            influxDBService.insertBotResponseTime(botResponseTime, botDepth, serverId);
            return new ResponseEntity<>("Bot response time inserted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to insert bot response time", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/server-load")
    public ResponseEntity<String> insertServerLoad(@RequestParam double cpuUsage, @RequestParam double memoryUsage, @RequestParam double diskUsage, @RequestParam int serverId) {
        try {
            influxDBService.insertServerLoad(cpuUsage, memoryUsage, diskUsage, serverId);
            return new ResponseEntity<>("Server load inserted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to insert server load", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}