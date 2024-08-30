package io.deeplay.camp.botfactory.service.bot;

import io.deeplay.camp.botfactory.service.BoardService;

public interface EvaluationStrategy {
    double evaluate(BoardService boardService, int currentPlayerId);
}