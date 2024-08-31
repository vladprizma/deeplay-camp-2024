package io.deeplay.camp.botfactory.service.bot;


import io.deeplay.camp.botfactory.service.BoardService;

/**
 * Evaluates the board state using a heuristic function.
 */
public class HeuristicEvaluatorStrategy implements EvaluationStrategy {
    private int stabilityWeight = 560;
    private int cornerWeight = 800;
    private int mobilityWeight = 80;
    private int pieceWeight = 10;
    private int frontierWeight = 70;

    private static int[][] getDynamicWeights(double gameProgress) {
        int[][] baseWeights = {
                {20, -3, 11, 8, 8, 11, -3, 20},
                {-3, -7, -4, 1, 1, -4, -7, -3},
                {11, -4, 2, 2, 2, 2, -4, 11},
                {8, 1, 2, -3, -3, 2, 1, 8},
                {8, 1, 2, -3, -3, 2, 1, 8},
                {11, -4, 2, 2, 2, 2, -4, 11},
                {-3, -7, -4, 1, 1, -4, -7, -3},
                {20, -3, 11, 8, 8, 11, -3, 20}
        };

        int[][] dynamicWeights = new int[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                dynamicWeights[i][j] = (int) (baseWeights[i][j] * (1 - gameProgress));
            }
        }

        return dynamicWeights;
    }

    /**
     * Evaluates the board state.
     *
     * @param boardService     The board state after the move.
     * @param currentPlayerId The ID of the current player.
     * @return The evaluation score of the board state.
     */
    @Override
    public double evaluate(BoardService boardService, int currentPlayerId) {
        //TODO нормировать возвратные значения
        if (boardService.checkForWin().isGameFinished()) {
            if (boardService.checkForWin().getUserIdWinner() == currentPlayerId) {
                return Double.POSITIVE_INFINITY - 1;
            } else if (boardService.checkForWin().getUserIdWinner() != currentPlayerId) {
                return Double.NEGATIVE_INFINITY + 1;
            } else {
                return 0;
            }
        }

        int opponentId = (currentPlayerId == 1) ? 2 : 1;

        int totalDiscs = boardService.getChips(1).size() + boardService.getChips(2).size();
        double gameProgress = (double) totalDiscs / (8 * 8);
        int[][] currentWeights = getDynamicWeights(gameProgress);

        int currentPlayerScore = 0;
        int opponentScore = 0;
        int frontierDiscs = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (boardService.hasPieceBlack(x, y)) {
                    if (currentPlayerId == 1) {
                        currentPlayerScore += currentWeights[x][y];
                    } else {
                        opponentScore += currentWeights[x][y];
                    }
                } else if (boardService.hasPieceWhite(x, y)) {
                    if (currentPlayerId == 2) {
                        currentPlayerScore += currentWeights[x][y];
                    } else {
                        opponentScore += currentWeights[x][y];
                    }
                }

                if (isFrontierDisc(boardService, x, y)) {
                    if ((currentPlayerId == 1 && boardService.hasPieceBlack(x, y)) ||
                            (currentPlayerId == 2 && boardService.hasPieceWhite(x, y))) {
                        frontierDiscs--;
                    } else {
                        frontierDiscs++;
                    }
                }
            }
        }

        int pieceDifference = boardService.getChips(currentPlayerId).size() - boardService.getChips(opponentId).size();
        int mobilityDifference = calculateMobility(boardService, currentPlayerId) - calculateMobility(boardService, opponentId);

        int[] stablePieces = countStablePieces(boardService);
        int stableDifference = stablePieces[currentPlayerId - 1] - stablePieces[opponentId - 1];

        int cornerControl = evaluateCornerControl(boardService, currentPlayerId, opponentId);

        return currentPlayerScore - opponentScore
                + pieceWeight * pieceDifference          // Coin Parity
                + mobilityWeight * mobilityDifference    // Mobility
                + frontierWeight * frontierDiscs         // Frontier Discs
                + stabilityWeight * stableDifference     // Stability
                + cornerWeight * cornerControl;          // Corner Captivity
    }

    /**
     * Calculates the mobility of the current player.
     *
     * @param board          The current board state.
     * @param currentPlayerId The ID of the current player.
     * @return The mobility score of the current player.
     */
    private static int calculateMobility(BoardService board, int currentPlayerId) {
        return board.getAllValidTiles(currentPlayerId).size();
    }

    /**
     * Counts the number of pieces flipped between two board states.
     *
     * @param boardBefore The board state before the move.
     * @param boardAfter  The board state after the move.
     * @return The number of pieces flipped.
     */
    private static int countFlippedPieces(BoardService boardBefore, BoardService boardAfter) {
        int flippedPieces = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (boardAfter.hasPieceBlack(x, y) && !boardBefore.hasPieceBlack(x, y)) {
                    flippedPieces++;
                } else if (boardAfter.hasPieceWhite(x, y) && !boardBefore.hasPieceWhite(x, y)) {
                    flippedPieces++;
                }
            }
        }

        return flippedPieces;
    }

    private static boolean isFrontierDisc(BoardService board, int x, int y) {
        int[] dx = {-1, 0, 1, 0, -1, 1, -1, 1};
        int[] dy = {0, -1, 0, 1, -1, -1, 1, 1};

        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8 && board.getPiece(nx, ny) == 0) {
                return true;
            }
        }

        return false;
    }

    private static int[] countStablePieces(BoardService board) {
        int[] stablePieces = new int[2];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (isStable(board, x, y)) {
                    if (board.hasPieceBlack(x, y)) {
                        stablePieces[0]++;
                    } else if (board.hasPieceWhite(x, y)) {
                        stablePieces[1]++;
                    }
                }
            }
        }

        return stablePieces;
    }

    private static boolean isStable(BoardService board, int x, int y) {
        int piece = board.getPiece(x, y);
        if (piece == 0) {
            return false;
        }

        boolean horizontal = checkLineStability(board, x, y, 1, 0) && checkLineStability(board, x, y, -1, 0);
        boolean vertical = checkLineStability(board, x, y, 0, 1) && checkLineStability(board, x, y, 0, -1);
        boolean diagonal1 = checkLineStability(board, x, y, 1, 1) && checkLineStability(board, x, y, -1, -1);
        boolean diagonal2 = checkLineStability(board, x, y, 1, -1) && checkLineStability(board, x, y, -1, 1);

        return horizontal && vertical && diagonal1 && diagonal2;
    }

    private static boolean checkLineStability(BoardService board, int x, int y, int dx, int dy) {
        int piece = board.getPiece(x, y);
        int newX = x + dx;
        int newY = y + dy;

        while (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
            if (board.getPiece(newX, newY) != piece) {
                return false;
            }
            newX += dx;
            newY += dy;
        }
        return true;
    }

    /**
     * Evaluates corner control for the current player and the opponent.
     *
     * @param board The current board state.
     * @param currentPlayerId The ID of the current player.
     * @param opponentId The ID of the opponent player.
     * @return The difference in corner control between the current player and the opponent.
     */
    private static int evaluateCornerControl(BoardService board, int currentPlayerId, int opponentId) {
        int[] corners = {
                board.getPiece(0, 0), board.getPiece(0, 7),
                board.getPiece(7, 0), board.getPiece(7, 7)
        };

        int currentPlayerCorners = 0;
        int opponentCorners = 0;

        for (int corner : corners) {
            if (corner == currentPlayerId) {
                currentPlayerCorners++;
            } else if (corner == opponentId) {
                opponentCorners++;
            }
        }

        return currentPlayerCorners - opponentCorners;
    }
}
