package io.deeplay.camp.bot;

import io.deeplay.camp.board.BoardService;

/**
 * Evaluates the board state using a heuristic function.
 */
public class HeuristicEvaluator {
    private static final int[][] WEIGHTS = {
            {20, -3, 11, 8, 8, 11, -3, 20},
            {-3, -7, -4, 1, 1, -4, -7, -3},
            {11, -4, 2, 2, 2, 2, -4, 11},
            {8, 1, 2, -3, -3, 2, 1, 8},
            {8, 1, 2, -3, -3, 2, 1, 8},
            {11, -4, 2, 2, 2, 2, -4, 11},
            {-3, -7, -4, 1, 1, -4, -7, -3},
            {20, -3, 11, 8, 8, 11, -3, 20}
    };

    private static int[][] getDynamicWeights(double gameProgress) {
        if (gameProgress < 0.2) {
            return new int[][] {
                    {20, -3, 11, 8, 8, 11, -3, 20},
                    {-3, -7, -4, 1, 1, -4, -7, -3},
                    {11, -4, 2, 2, 2, 2, -4, 11},
                    {8, 1, 2, -3, -3, 2, 1, 8},
                    {8, 1, 2, -3, -3, 2, 1, 8},
                    {11, -4, 2, 2, 2, 2, -4, 11},
                    {-3, -7, -4, 1, 1, -4, -7, -3},
                    {20, -3, 11, 8, 8, 11, -3, 20}
            };
        } else if (gameProgress < 0.8) {
            return new int[][] {
                    {16, -2, 8, 6, 6, 8, -2, 16},
                    {-2, -5, -3, 0, 0, -3, -5, -2},
                    {8, -3, 1, 1, 1, 1, -3, 8},
                    {6, 0, 1, -2, -2, 1, 0, 6},
                    {6, 0, 1, -2, -2, 1, 0, 6},
                    {8, -3, 1, 1, 1, 1, -3, 8},
                    {-2, -5, -3, 0, 0, -3, -5, -2},
                    {16, -2, 8, 6, 6, 8, -2, 16}
            };
        } else {
            return new int[][] {
                    {12, -1, 5, 4, 4, 5, -1, 12},
                    {-1, -3, -2, 0, 0, -2, -3, -1},
                    {5, -2, 0, 0, 0, 0, -2, 5},
                    {4, 0, 0, -1, -1, 0, 0, 4},
                    {4, 0, 0, -1, -1, 0, 0, 4},
                    {5, -2, 0, 0, 0, 0, -2, 5},
                    {-1, -3, -2, 0, 0, -2, -3, -1},
                    {12, -1, 5, 4, 4, 5, -1, 12}
            };
        }
    }

    /**
     * Evaluates the board state.
     *
     * @param boardBefore    The board state before the move.
     * @param boardAfter     The board state after the move.
     * @param currentPlayerId The ID of the current player.
     * @return The evaluation score of the board state.
     */
    public double heuristic(BoardService boardBefore, BoardService boardAfter, int currentPlayerId) {
        int opponentId = (currentPlayerId == 1) ? 2 : 1;

        int totalDiscs = boardAfter.getChips(1).size() + boardAfter.getChips(2).size();
        double gameProgress = (double) totalDiscs / (8 * 8);
        int[][] currentWeights = getDynamicWeights(gameProgress);
        
        int currentPlayerScore = 0;
        int opponentScore = 0;
        int frontierDiscs = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (boardAfter.hasPieceBlack(x, y)) {
                    if (currentPlayerId == 1) {
                        currentPlayerScore += currentWeights[x][y];
                    } else {
                        opponentScore += currentWeights[x][y];
                    }
                } else if (boardAfter.hasPieceWhite(x, y)) {
                    if (currentPlayerId == 2) {
                        currentPlayerScore += currentWeights[x][y];
                    } else {
                        opponentScore += currentWeights[x][y];
                    }
                }

                // Учитываем количество фишек на границе
                if (isFrontierDisc(boardAfter, x, y)) {
                    if ((currentPlayerId == 1 && boardAfter.hasPieceBlack(x, y)) ||
                            (currentPlayerId == 2 && boardAfter.hasPieceWhite(x, y))) {
                        frontierDiscs--;
                    } else {
                        frontierDiscs++;
                    }
                }
            }
        }

        int pieceDifference = boardAfter.getChips(currentPlayerId).size() - boardAfter.getChips(opponentId).size();
        int mobilityDifference = calculateMobility(boardAfter, currentPlayerId) - calculateMobility(boardAfter, opponentId);
        int flipScore = countFlippedPieces(boardBefore, boardAfter);

        int[] stablePieces = countStablePieces(boardAfter);
        int stableDifference = stablePieces[currentPlayerId - 1] - stablePieces[opponentId - 1];

        int cornerControl = evaluateCornerControl(boardAfter, currentPlayerId, opponentId);

        return currentPlayerScore - opponentScore
                + 10 * pieceDifference          // Coin Parity
                + 78 * mobilityDifference    // Mobility
                + 74 * frontierDiscs          // Frontier Discs
                + 561 * stableDifference        // Stability
                + 801 * cornerControl;          // Corner Captivity
    }

    /**
     * Calculates the mobility of the current player.
     *
     * @param board          The current board state.
     * @param currentPlayerId The ID of the current player.
     * @return The mobility score of the current player.
     */
    private int calculateMobility(BoardService board, int currentPlayerId) {
        return board.getAllValidTiles(currentPlayerId).size();
    }

    /**
     * Counts the number of pieces flipped between two board states.
     *
     * @param boardBefore The board state before the move.
     * @param boardAfter  The board state after the move.
     * @return The number of pieces flipped.
     */
    private int countFlippedPieces(BoardService boardBefore, BoardService boardAfter) {
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

    /**
     * Определяет, является ли фишка Frontier Disc (имеет ли она рядом пустые клетки).
     */
    private boolean isFrontierDisc(BoardService board, int x, int y) {
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

    /**
     * Подсчитывает количество стабильных фишек для каждого игрока.
     *
     * @param board The current board state.
     * @return Массив с количеством стабильных фишек [для черных, для белых].
     */
    private int[] countStablePieces(BoardService board) {
        int[] stablePieces = new int[2]; // stablePieces[0] - для черных, stablePieces[1] - для белых

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

    /**
     * Определяет, является ли фишка стабильной.
     */
    private boolean isStable(BoardService board, int x, int y) {
        int piece = board.getPiece(x, y);
        if (piece == 0) {
            return false; // Пустая клетка не может быть стабильной
        }

        // Проверяем, окружена ли фишка такими же фишками по горизонтали, вертикали и диагоналям
        boolean horizontal = checkLineStability(board, x, y, 1, 0) && checkLineStability(board, x, y, -1, 0);
        boolean vertical = checkLineStability(board, x, y, 0, 1) && checkLineStability(board, x, y, 0, -1);
        boolean diagonal1 = checkLineStability(board, x, y, 1, 1) && checkLineStability(board, x, y, -1, -1);
        boolean diagonal2 = checkLineStability(board, x, y, 1, -1) && checkLineStability(board, x, y, -1, 1);

        return horizontal && vertical && diagonal1 && diagonal2;
    }

    /**
     * Проверяет стабильность фишки в заданной линии.
     */
    private boolean checkLineStability(BoardService board, int x, int y, int dx, int dy) {
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
    private int evaluateCornerControl(BoardService board, int currentPlayerId, int opponentId) {
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
