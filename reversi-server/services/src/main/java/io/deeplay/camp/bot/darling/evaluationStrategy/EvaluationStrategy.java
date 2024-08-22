package io.deeplay.camp.bot.darling.evaluationStrategy;

import io.deeplay.camp.board.BoardService;

public interface EvaluationStrategy {
    double evaluate(BoardService boardService, int currentPlayerId);
}