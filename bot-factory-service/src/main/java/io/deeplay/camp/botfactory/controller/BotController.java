package io.deeplay.camp.botfactory.controller;

import io.deeplay.camp.botfactory.dto.BotMoveRequest;
import io.deeplay.camp.botfactory.dto.BotMoveResponse;
import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.BoardService;
import io.deeplay.camp.botfactory.service.bot.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling bot-related requests.
 */
@RestController
@RequestMapping("/bot")
public class BotController {

    private final BotService botService;

    /**
     * Constructs a new BotController with the specified BotService.
     *
     * @param botService The service used to get bot moves.
     */
    @Autowired
    public BotController(BotService botService) {
        this.botService = botService;
    }
    
    /**
     * Handles the request to get the bot's move.
     *
     * @param request The request containing the current board state and player information.
     * @return The response containing the bot's move.
     */
    @PostMapping("/move")
    public BotMoveResponse getBotMove(@RequestBody BotMoveRequest request) {
        var boardService = new BoardService(request.getBoard());
        Tile move = botService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }
}