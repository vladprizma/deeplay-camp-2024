package io.deeplay.camp.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.dto.BotMoveRequest;
import io.deeplay.camp.dto.BotMoveResponse;
import io.deeplay.camp.entity.Board;
import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.enums.Bots;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

public class BotService extends BotStrategy {
    private static final String BOT_FACTORY_URL = "http://localhost:8082/bot";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient;
    private static boolean serverAvailable = true;

    public BotService(int id, String name, Bots bot) {
        super(id, name, bot);
        this.httpClient = HttpClientProvider.getHttpClient();
    }

    private String getBotMoveUrl(Bots bot) {
        return switch (bot) {
            case DARLING -> BOT_FACTORY_URL + "/darling/minimax/move";
            case VIOLA -> BOT_FACTORY_URL + "/viola/minimax/move";
            case ANDREY -> BOT_FACTORY_URL + "/andrey/minimax/move";
            default -> BOT_FACTORY_URL + "/random/minimax/move";
        };
    }

    private Tile executeBotMoveRequest(String url, BotMoveRequest request) throws IOException {
        if (!serverAvailable) {
            return getBackupBotMove(request);
        }

        String requestBody = objectMapper.writeValueAsString(request);
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(requestBody));
        httpPost.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getCode() == 200) {
                BotMoveResponse botMoveResponse = objectMapper.readValue(response.getEntity().getContent(), BotMoveResponse.class);
                return botMoveResponse.getMove();
            } else {
                return getBackupBotMove(request);
            }
        } catch (SocketTimeoutException e) {
            serverAvailable = false;
            return getBackupBotMove(request);
        } catch (IOException e) {
            return getBackupBotMove(request);
        }
    }

    public Tile getBotMove(Board board, int currentPlayerId) throws IOException {
        BotMoveRequest request = new BotMoveRequest(board, currentPlayerId);
        String url = getBotMoveUrl(bot);
        return executeBotMoveRequest(url, request);
    }

    private Tile getBackupBotMove(BotMoveRequest request) {
        return new RandomBot(2, "RandomBot", Bots.RANDOM).getMove(request.getCurrentPlayerId(), new BoardService(request.getBoard()));
    }
    
    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        try {
            return getBotMove(boardLogic.getBoard(), currentPlayerId);
        } catch (IOException e) {
            throw new RuntimeException("Error while getting bot move", e);
        }
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return List.of();
    }
}
