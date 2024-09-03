package io.deeplay.camp.botfactory.controller;

import io.deeplay.camp.botfactory.dto.BotMoveRequest;
import io.deeplay.camp.botfactory.dto.BotMoveResponse;
import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bot")
public class BotController {

    private final BotService botService;

    @Autowired
    public BotController(BotService botService) {
        this.botService = botService;
    }

    @PostMapping("/darling/minimax/move")
    public BotMoveResponse getBotMove(@RequestBody BotMoveRequest request) {
        botService.setMiniMaxDarlingStrategy();
        var boardService = new BoardService(request.getBoard());
        Tile move = botService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }

    @PostMapping("/viola/minimax/move")
    public BotMoveResponse getMiniMaxBotMove(@RequestBody BotMoveRequest request) {
        botService.setMiniMaxBotStrategy();
        var boardService = new BoardService(request.getBoard());
        Tile move = botService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }

    @PostMapping("/viola/expectimax/move")
    public BotMoveResponse getExpectiMaxBotMove(@RequestBody BotMoveRequest request) {
        botService.setExpectiMaxBotStrategy();
        var boardService = new BoardService(request.getBoard());
        Tile move = botService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }

    @PostMapping("/viola/montecarlo/move")
    public BotMoveResponse getMonteCarloBotMove(@RequestBody BotMoveRequest request) {
        botService.setMonteCarloBotStrategy();
        var boardService = new BoardService(request.getBoard());
        Tile move = botService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }
}