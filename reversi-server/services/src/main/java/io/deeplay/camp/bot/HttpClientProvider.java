package io.deeplay.camp.bot;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;

import java.util.concurrent.TimeUnit;

public class HttpClientProvider {

    private static final CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(100);

        httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    private HttpClientProvider() {}

    public static CloseableHttpClient getHttpClient() {
        return httpClient;
    }
}
