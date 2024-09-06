package io.deeplay.camp.metrics;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MetricsService {
    private final String metricsServiceUrl;

    public MetricsService(String metricsServiceUrl) {
        this.metricsServiceUrl = metricsServiceUrl;
    }

    private void sendPutRequest(String endpoint, String params) throws Exception {
        URL url = new URL(metricsServiceUrl + endpoint + "?" + params);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(params.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    public void insertResponseTime(long responseTime, int totalUsers, int serverId) throws Exception {
        String params = String.format("responseTime=%d&totalUsers=%d&serverId=%d", responseTime, totalUsers, serverId);
        sendPutRequest("/api/metrics/response-time", params);
    }

    public void insertThroughput(int throughput, int serverId) throws Exception {
        String params = String.format("throughput=%d&serverId=%d", throughput, serverId);
        sendPutRequest("/api/metrics/throughput", params);
    }

    public void insertErrorRate(double errorRate, int serverId) throws Exception {
        String params = String.format("errorRate=%f&serverId=%d", errorRate, serverId);
        sendPutRequest("/api/metrics/error-rate", params);
    }

    public void insertGameSessionsCount(int gameSessionsCount, int serverId) throws Exception {
        String params = String.format("gameSessionsCount=%d&serverId=%d", gameSessionsCount, serverId);
        sendPutRequest("/api/metrics/game-sessions-count", params);
    }

    public void insertBotResponseTime(long botResponseTime, int botDepth, int serverId) throws Exception {
        String params = String.format("botResponseTime=%d&botDepth=%d&serverId=%d", botResponseTime, botDepth, serverId);
        sendPutRequest("/api/metrics/bot-response-time", params);
    }

    public void insertServerLoad(double cpuUsage, double memoryUsage, double diskUsage, int serverId) throws Exception {
        String params = String.format("cpuUsage=%f&memoryUsage=%f&diskUsage=%f&serverId=%d", cpuUsage, memoryUsage, diskUsage, serverId);
        sendPutRequest("/api/metrics/server-load", params);
    }
}