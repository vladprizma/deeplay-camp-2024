package io.deeplay.camp.metrics.service;

public interface MetricsStrategy {
    public void insertResponseTime(long responseTime, int totalUsers, int serverId);
    public void insertThroughput(int throughput, int serverId);
    public void insertErrorRate(double errorRate, int serverId);
    public void insertGameSessionsCount(int gameSessionsCount, int serverId);
    public void insertBotResponseTime(long botResponseTime, int botDepth, int serverId);
    public void insertServerLoad(double cpuUsage, double memoryUsage, double diskUsage, int serverId);
}
