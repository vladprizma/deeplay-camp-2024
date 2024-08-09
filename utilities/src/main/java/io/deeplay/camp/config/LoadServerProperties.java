package io.deeplay.camp.config;

import io.deeplay.camp.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadServerProperties {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static boolean badInputConfig = false;
    
    public static Properties loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("server.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            logger.error("Ошибка в загрузке конфига сервера. Пожалуйста, проверьте правильность настройки пути к файлу.");
        }

        var ip = properties.getProperty("server.ip");
        var port = Integer.parseInt(properties.getProperty("server.port"));
        var selfPlay = Boolean.getBoolean(properties.getProperty("server.self-play"));
        Properties propertiesBadConfig = new Properties();
        
        if (!ValidateServerProperties.validateIpAddress(ip)) {
            propertiesBadConfig.put("server.ip", "bad");
            
            logger.error("Неверно введен ip. Проверьте файл: server.properties");
            
            badInputConfig = true;
        }
        if (!ValidateServerProperties.isValidPort(port)) {
            propertiesBadConfig.put("server.port", "bad");

            logger.error("Неверно введен port. Проверьте файл: server.properties");
            
            badInputConfig = true;
        }
        
        if (badInputConfig) return propertiesBadConfig;
        
        return properties;
    }
}
