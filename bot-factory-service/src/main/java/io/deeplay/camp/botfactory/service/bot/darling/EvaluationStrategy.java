package io.deeplay.camp.botfactory.service.bot.darling;

import io.deeplay.camp.botfactory.service.board.BoardService;

public interface EvaluationStrategy {
    double evaluate(BoardService boardService, int currentPlayerId);
}