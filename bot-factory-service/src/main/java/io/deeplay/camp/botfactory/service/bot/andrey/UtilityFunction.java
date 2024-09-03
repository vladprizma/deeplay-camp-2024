package io.deeplay.camp.botfactory.service.bot.andrey;

import io.deeplay.camp.botfactory.service.board.BoardService;

public interface UtilityFunction {
    double evaluate(BoardService boardAfter, int currentPlayerId);
}
