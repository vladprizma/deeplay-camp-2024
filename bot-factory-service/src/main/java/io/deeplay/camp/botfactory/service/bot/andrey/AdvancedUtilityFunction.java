package io.deeplay.camp.botfactory.service.bot.andrey;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;

import java.util.List;

public class AdvancedUtilityFunction implements UtilityFunction {
    private static final int[][] WEIGHTS = {
            {200, -10, 30, 30, 30, 30, -10, 200},
            {-10, -20, 1, 1, 1, 1, -20, -10},
            {30, 1, 5, 5, 5, 5, 1, 30},
            {30, 1, 5, 5, 5, 5, 1, 30},
            {30, 1, 5, 5, 5, 5, 1, 30},
            {30, 1, 5, 5, 5, 5, 1, 30},
            {-10, -20, 1, 1, 1, 1, -20, -10},
            {200, -10, 30, 30, 30, 30, -10, 200}
    };

    private static final int MAX_SCORE = 1000;

    @Override
    public double evaluate(BoardService boardAfter, int currentPlayerId) {
        int opponentId = (currentPlayerId == 1) ? 2 : 1;

        if (boardAfter.checkForWin().isGameFinished()) {
            if (boardAfter.checkForWin().getUserIdWinner() == currentPlayerId) {
                return 1.0;
            } else if (boardAfter.checkForWin().getUserIdWinner() == opponentId) {
                return -1.0;
            } else if (boardAfter.checkForWin().getUserIdWinner() == 3) {
                return 0.0;
            }
        }

        int currentPlayerScore = 0;
        int opponentScore = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (currentPlayerId == 1) {
                    if (boardAfter.hasPieceBlack(x, y)) {
                        currentPlayerScore += WEIGHTS[x][y];
                    } else if (boardAfter.hasPieceWhite(x, y)) {
                        opponentScore += WEIGHTS[x][y];
                    }
                } else {
                    if (boardAfter.hasPieceBlack(x, y)) {
                        opponentScore += WEIGHTS[x][y];
                    } else if (boardAfter.hasPieceWhite(x, y)) {
                        currentPlayerScore += WEIGHTS[x][y];
                    }
                }
            }
        }

        int blackCount = boardAfter.getBlackChipsCount();
        int whiteCount = boardAfter.getWhiteChipsCount();

        int currentPlayerPieces = currentPlayerId == 1 ? blackCount : whiteCount;
        int opponentPieces = opponentId == 1 ? blackCount : whiteCount;
        int pieceDifference = currentPlayerPieces - opponentPieces;

        int currentPlayerMobility = calculateMobility(boardAfter, currentPlayerId);
        int opponentMobility = calculateMobility(boardAfter, opponentId);
        int mobilityDifference = currentPlayerMobility - opponentMobility;

        double rawScore = currentPlayerScore - opponentScore
                + 4 * pieceDifference
                + 20 * mobilityDifference;

        return Math.max(-1.0, Math.min(1.0, rawScore / MAX_SCORE));
    }

    private int calculateMobility(BoardService board, int currentPlayerId) {
        List<Tile> validMoves = board.getAllValidTiles(currentPlayerId);
        return validMoves.size();
    }
}
