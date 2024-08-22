package io.deeplay.camp.bot;

import io.deeplay.camp.board.BoardService;

public interface EvaluationStrategy {
    double evaluate(BoardService boardService, int currentPlayerId);
}