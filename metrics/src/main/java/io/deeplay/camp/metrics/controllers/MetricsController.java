package io.deeplay.camp.metrics.controllers;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.*;

@RestController
public class MetricsController {
    public class GameResult {
        private String bot1;
        private String bot2;
        private String result;

        // Геттеры и сеттеры
        public String getBot1() {
            return bot1;
        }

        public void setBot1(String bot1) {
            this.bot1 = bot1;
        }

        public String getBot2() {
            return bot2;
        }

        public void setBot2(String bot2) {
            this.bot2 = bot2;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
    
    private final MeterRegistry meterRegistry;

    public MetricsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/record-game")
    public String recordGame(@RequestParam String bot1, @RequestParam String bot2, @RequestParam String result) {
        String metricName = "games_" + bot1 + "_vs_" + bot2;

        switch (result) {
            case "bot1":
                meterRegistry.counter(metricName + "_bot1_wins").increment();
                break;
            case "bot2":
                meterRegistry.counter(metricName + "_bot2_wins").increment();
                break;
            case "draw":
                meterRegistry.counter(metricName + "_draws").increment();
                break;
            default:
                return "Invalid result";
        }
        return "Game recorded";
    }

    @PostMapping("/record-game-add")
    public String recordGame(@RequestBody GameResult gameResult) {
        String metricName = "games_" + gameResult.getBot1() + "_vs_" + gameResult.getBot2();

        switch (gameResult.getResult()) {
            case "bot1":
                meterRegistry.counter(metricName + "_bot1_wins").increment();
                break;
            case "bot2":
                meterRegistry.counter(metricName + "_bot2_wins").increment();
                break;
            case "draw":
                meterRegistry.counter(metricName + "_draws").increment();
                break;
            default:
                return "Invalid result";
        }
        return "Game recorded";
    }
}
