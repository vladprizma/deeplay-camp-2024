package io.deeplay.camp.botfactory.controller;

import io.deeplay.camp.botfactory.dto.BotMoveRequest;
import io.deeplay.camp.botfactory.dto.BotMoveResponse;
import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.AndreyBotService;
import io.deeplay.camp.botfactory.service.bot.DarlingBotService;
import io.deeplay.camp.botfactory.service.bot.ViolaBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/bot")
public class BotController {

    private final DarlingBotService botService;
    private final AndreyBotService andreyBotService;
    private final ViolaBotService violaBotService;

    @Autowired
    public BotController(DarlingBotService botService, AndreyBotService andreyBotService, ViolaBotService violaBotService) {
        this.botService = botService;
        this.andreyBotService = andreyBotService;
        this.violaBotService = violaBotService;
    }

    @PostMapping("/darling/minimax/move")
    public BotMoveResponse getBotMove(@RequestBody BotMoveRequest request) {
        var boardService = new BoardService(request.getBoard());
        Tile move = botService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }

    @PostMapping("/viola/minimax/move")
    public BotMoveResponse getMiniMaxBotMove(@RequestBody BotMoveRequest request) {
        violaBotService.setMiniMaxViolaStrategy();
        var boardService = new BoardService(request.getBoard());
        Tile move = violaBotService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }
    
    @PostMapping("/andrey/minimax/move")
    public BotMoveResponse getAndreyMiniMaxBotMove(@RequestBody BotMoveRequest request) {
        andreyBotService.setMCTSbot();
        var boardService = new BoardService(request.getBoard());
        Tile move = andreyBotService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }

    @PostMapping("/viola/expectimax/move")
    public BotMoveResponse getExpectiMaxBotMove(@RequestBody BotMoveRequest request) {
        violaBotService.setExpectiMaxViolaStrategy();
        var boardService = new BoardService(request.getBoard());
        Tile move = violaBotService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }

    @PostMapping("/viola/montecarlo/move")
    public BotMoveResponse getMonteCarloBotMove(@RequestBody BotMoveRequest request) {
        violaBotService.setMonteCarloViolaStrategy();
        var boardService = new BoardService(request.getBoard());
        Tile move = violaBotService.getBotMove(request.getCurrentPlayerId(), boardService);
        BotMoveResponse response = new BotMoveResponse(move);
        response.setMove(move);
        return response;
    }
}